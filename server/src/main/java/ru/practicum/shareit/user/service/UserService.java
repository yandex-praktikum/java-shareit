package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;


public interface UserService {

    Collection<UserDto> getAllUsers();

    UserDto create(UserDto userDto);

    UserDto getById(long id);

    UserDto update(long id, UserDto userDto);

    void delete(long id);
}