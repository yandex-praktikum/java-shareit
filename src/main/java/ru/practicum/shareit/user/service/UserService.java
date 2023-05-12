package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);

    List<UserDto> findAll();

    UserDto findById(Long userId);

    UserDto updateById(UserDto userDto, Long userId);

    void delete(Long userId);
}