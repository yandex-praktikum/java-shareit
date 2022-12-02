package ru.practicum.shareit.user;

import java.util.List;

public interface UserDao {
    User getUserById(Long usedId);
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(User user);
    boolean deleteUser(Long userId);
}
