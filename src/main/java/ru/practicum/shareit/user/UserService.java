package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    List<User> getAllUsers();

    User getUserById(int userId);

    void deleteUser(int id);

    User updateUser(int id, User user);

}
