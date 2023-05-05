package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(long id);

    UserDto saveNewUser(UserDto userDto);

    UserDto updateUser(long id, UserDto userDto);

    void deleteUser(long id);
}
