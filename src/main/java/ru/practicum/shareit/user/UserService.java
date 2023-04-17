package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto create(UserDto userDto);

    void deleteUser(Long userId);

    UserDto findUserById(Long userId);

    UserDto updateUser(Long userId, UserDto inputUser);
}
