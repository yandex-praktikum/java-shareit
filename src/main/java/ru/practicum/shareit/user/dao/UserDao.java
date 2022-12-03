package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User getUserById(Long usedId);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(Long userId, User user);

    boolean deleteUser(Long userId);
}
