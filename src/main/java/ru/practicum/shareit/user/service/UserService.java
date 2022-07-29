package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Интерфейс для сервиса вещей
 */
public interface UserService {

    List<UserDto> getUserAll();

    UserDto getUserById(long userId);

    UserDto add(UserDto userDto);

    UserDto update(long userId, UserDto userDto);

    void delete(long userId);
}
