package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();

    User getUser(Long userId);

    User createUser(User user);

    User updateUser(User updatedUser);

    void deleteUser(Long userId);
}
