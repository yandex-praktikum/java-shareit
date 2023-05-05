package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    UserDto createUser(User user);

    UserDto changeUser(int userId, UserDto userDto);

    UserDto findUserById(int id);

    void removeUser(int id);
}
