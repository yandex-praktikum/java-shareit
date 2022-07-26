package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    List<User> getAllUsers();

    User getUserById(int userId);

    void deleteUser(int id);
}
