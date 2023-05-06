package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {
    private static int generatorId = 0;
    private final List<User> users = new ArrayList<>();

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return users.stream().filter(user -> user.getId() == userId).findFirst();
    }

    @Override
    public User saveNewUser(User user) {
        user.setId(++generatorId);
        users.add(user);
        return user;
    }

    @Override
    public void deleteUser(long userId) {
        if (!getUserById(userId).isPresent()) {
            throw new UserNotFoundException("Пользователь не найден.");
        }
        users.remove(getUserById(userId).get());
    }
}
