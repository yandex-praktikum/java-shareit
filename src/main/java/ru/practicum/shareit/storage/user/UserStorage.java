package ru.practicum.shareit.storage.user;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserStorage {
    User getUserById(Integer id);

    User addUser(User user);

    User updateUser(User user, int userId);

    List<User> getAllUsers();

    void deleteUser(int id);

    boolean isDuplicateByEmail(User userToCheck);
}
