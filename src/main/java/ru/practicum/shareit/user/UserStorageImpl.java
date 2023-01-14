package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EmailException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserStorageImpl implements UserStorage {
    private Long counter = 1L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User get(Long id) {
        checkId(id);
        return users.get(id);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User add(User user) {
        validateEmail(user);
        user.setId(counter++);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User patch(User user) {
        checkId(user.getId());
        validateEmail(user);
        User updatedUser = users.get(user.getId());
        if (user.getName() != null && !user.getName().isEmpty()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            updatedUser.setEmail(user.getEmail());
        }
        users.put(updatedUser.getId(), updatedUser)
        return users.get(updatedUser.getId());
    }

    @Override
    public Boolean delete(Long id) {
        checkId(id);
        users.remove(id);
        return !users.containsKey(id);
    }

    private void checkId(Long id) {
        if (id != 0 && !users.containsKey(id)) {
            throw new NotFoundException("Пользователь с id: " + id + "не зарегистрирован");
        }
    }

    private void validateEmail(User user) {
        if (users.values().stream().anyMatch(stored -> stored.getEmail().equalsIgnoreCase(user.getEmail())
                && stored.getId() != user.getId())) {
            throw new EmailException("Пользователь с таким Email " + user.getEmail() + " уже существует!");
        }
    }
}
