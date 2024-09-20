package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

public interface UserRepository {
    User addUser(User user);

    Long validationEmail(User user);

    boolean validationUserId(long userId);

    User updateUser(long userId, User user);

    User getUserById(long userId);

    void deleteUserById(long userId);
}
