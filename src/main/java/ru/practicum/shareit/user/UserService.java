package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto create(UserDto userDto);

    void deleteUser(Long id);

    UserDto findUserById(Long userId);
}
