package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto create(UserDto userDto);

    Optional<UserDto> update(Long id, UserDto userDto);

    Optional<UserDto> getUserById(Long id);

    List<UserDto> getAll();

    void deleteUserById(Long id);

}
