package ru.practicum.shareit.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Data
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private Long count = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        count++;
        user.setId(count);
        users.put(count, user);
        return user;
    }

    @Override
    public Long validationEmail(User user) {
        return users.values().stream().filter(x -> x.getEmail().equals(user.getEmail())).count();
    }

    @Override
    public boolean validationUserId(long userId) {
        return users.containsKey(userId);
    }

    @Override
    public User updateUser(long userId, User user) {
        User userForUpdate = users.get(userId);
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userForUpdate.setEmail(user.getEmail());
        }
        users.put(userId, userForUpdate);
        return userForUpdate;
    }

    @Override
    public User getUserById(long userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        users.remove(userId);
    }
}
