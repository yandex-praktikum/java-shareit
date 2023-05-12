package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserStorageDao implements UserDao {
    private final Map<Long, User> userMap = new HashMap<>();
    private Long userId = 1L;

    @Override
    public User create(User user) {
        User userFromMap = userMap.get(user.getId());

        if (Objects.isNull(userFromMap)) {
            user.setId(userId);
            userId += 1;
            userMap.put(user.getId(), user);

            return user;
        }

        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User update(User user, Long userId) {
        userMap.put(userId, user);

        return user;
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }
}