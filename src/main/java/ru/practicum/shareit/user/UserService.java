package ru.practicum.shareit.user;

import ru.practicum.shareit.exeptions.UserEmailValidationException;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto) throws UserEmailValidationException;

    UserDto updateUser(Integer userId, UserDto userDto) throws UserEmailValidationException, ValidationException;

    List<UserDto> findAllUsers() throws ValidationException;

    UserDto findUserById(Integer userId) throws ValidationException;

    void deleteUserById(int userId) throws ValidationException;
}
