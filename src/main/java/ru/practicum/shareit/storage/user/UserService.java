package ru.practicum.shareit.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.List;

@Service
public class UserService implements UserServiceInterface {
    @Autowired
    UserStorage userStorage;

    @Override
    public User addUser(User user) {
        System.out.println("Add user Service before valid");
        isValidUser(user);
        System.out.println("Add user Service after valid");
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user, int userId) {
        if (userStorage.updateUser(user, userId) == null) {
            throw new BadRequestException("Такого пользователя не существует");
        } else {
            return userStorage.getUserById(userId);
        }
    }

    @Override
    public User getUserById(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new BadRequestException("Такого пользователя не существует");
        } else {
            return userStorage.getUserById(id);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public void deleteUserById(int id) {
        userStorage.deleteUser(id);
    }

    void isValidUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Не задано имя пользователя");
        } else if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new BadRequestException("Не задан e-mail пользователя");
        }
    }
}
