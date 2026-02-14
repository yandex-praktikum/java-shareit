package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long idCounter = 1;

    public User create(User user) {
        boolean emailExists = users.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));

        if (emailExists) {
            throw new DuplicateEmailException("Email already exists");
        }

        long id = idCounter++;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь не найден");
        }

        User existingUser = users.get(user.getId());

        if (user.getEmail() != null) {
            boolean emailExists = users.values().stream()
                    .filter(u -> !u.getId().equals(user.getId()))
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(user.getEmail()));

            if (emailExists) {
                throw new DuplicateEmailException("Такой email уже существует");
            }
        }

        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        users.put(existingUser.getId(), existingUser);
        return existingUser;
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public boolean delete(Long id) {
        return users.remove(id) != null;
    }
}