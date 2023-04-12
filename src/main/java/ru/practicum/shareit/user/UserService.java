package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers();
    UserDto create(UserDto userDto) throws UserAlreadyExistsException;
    void deleteUser(Long id);
}
