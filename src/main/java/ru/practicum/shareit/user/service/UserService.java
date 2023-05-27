package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto user);

    void deleteUser(Integer userId);

    UserDto updateUser(Integer id, UserDto user);

    List<UserDto> getUsersList();

    UserDto getUser(Integer id);
}
