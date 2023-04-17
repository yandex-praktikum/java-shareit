package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAllUsers();

    User create(User user);

    void deleteUser(Long userId);

    User findUserById(Long userId);

    User updateUser(User userDtoInUser);
}
