package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> usersById = new HashMap<>();
    long id = 0;

    @Override
    public User createUser(User user) {
        User userWithId = user.withId(++id);
        usersById.put(id, userWithId);
        return userWithId;
    }

    @Override
    public User getUserById(Long id) {
        return usersById.get(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(usersById.values());
    }

    @Override
    public User updateUser(User user) {
        usersById.put(user.getId(), user);
        return user;
    }

    @Override
    public void removeUserById(Long id) {
        usersById.remove(id);
    }
}