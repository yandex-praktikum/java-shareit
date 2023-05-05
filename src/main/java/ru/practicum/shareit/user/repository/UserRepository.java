package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAllUsers();

    UserDto addUser(User user);

    UserDto updateUser(int userId, UserDto userDto);

    Optional<User> findById(int id);

    void deleteUser(int id);
}
