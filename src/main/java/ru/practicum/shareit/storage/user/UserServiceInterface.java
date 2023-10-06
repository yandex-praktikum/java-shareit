package ru.practicum.shareit.storage.user;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserServiceInterface {
    User addUser(User user);

    User updateUser(User user, int userId);

    User getUserById(int id);

    List<User> getAllUsers();

    void deleteUserById(int id);
}
