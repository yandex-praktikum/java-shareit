package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserSaveDto;

public interface UserService {

    UserDto createUser(UserSaveDto userSaveDto);

    UserDto getUser(Integer userId);

    UserDto updateUser(Integer userId, UserSaveDto userSaveDto);

    void deleteUser(Integer userId);
}
