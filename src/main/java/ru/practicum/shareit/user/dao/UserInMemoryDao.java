package ru.practicum.shareit.user.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Component("userInMemoryDao")
public class UserInMemoryDao implements UserDaoStorage {
    private Map<Long, User> users;

    @Override
    public User addToUserMap(User user) {
        users.put(user.getId(), user);
        return getUserById(user.getId()).orElseThrow(
                () -> new RuntimeException("Ошибка при добавлении юзера"));
    }

    @Override
    public Optional<User> update(Long id, User userFromRequest) {
        Optional<User> userForUpdate = getUserById(id);
        if (userForUpdate.isPresent()) {
            userBuild(userFromRequest, userForUpdate.get());
            return getUserById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        for (Long ids : users.keySet()) {
            if (ids.equals(id)) {
                return Optional.ofNullable(users.get(id));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = getUserById(id);
        if (user.isPresent()) {
            users.remove(id, users.get(id));
        }
    }

    private void userBuild(User userFromRequest, User userForUpdate) {
        if (userFromRequest.getName() != null) {
            userForUpdate.setName(userFromRequest.getName());
        }
        if (userFromRequest.getEmail() != null) {
            userForUpdate.setEmail(userFromRequest.getEmail());
        }
    }
}
