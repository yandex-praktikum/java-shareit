package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Integer userId, UserDto userDto);

    List<UserDto> findAllUsers();

    UserDto findUserDtoById(Integer userId);

    User findUserById(Integer userId);

    void deleteUserById(int userId);
}
