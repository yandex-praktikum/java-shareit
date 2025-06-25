package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    User saveUser(UserDto userDto);

    User updateUser(Long userId, UserDto userDto);

    User findByIdUser(long id);

    Collection<UserDto> findAllUser();

    void deleteUser(long id);
}
