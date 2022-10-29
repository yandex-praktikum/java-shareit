package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> userMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getById(Long id) {
            return userMap.get(id);
    }

    @Override
    public User create(User user) {
        User user1 = user;
        id++;
        user1.setId(id);
        userMap.put(id, user1);
        return user1;
    }

    @Override
    public User update(User user) {
        userMap.replace(user.getId(), user);
        return user;
//        return userMap.get(user.getId());
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }
}
