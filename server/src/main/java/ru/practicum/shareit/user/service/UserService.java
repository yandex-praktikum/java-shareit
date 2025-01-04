package ru.practicum.shareit.user.service;

import ru.practicum.shareit.base.service.BaseService;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService extends BaseService<User, Long> {
    UserDto create(RequestUserDto user);

    UserDto update(Long userId, RequestUserDto user);

    Optional<UserDto> findByEmail(String email);

    UserDto findUserById(Long id);

    void checkExistsById(Long id);

    boolean existsById(Long id);

    Collection<UserDto> getAll();
}
