package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserStorage userStorage;

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(long userId, User user) {
        return userStorage.updateUser(userId, user);
    }

    @Override
    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        userStorage.deleteUserById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}
