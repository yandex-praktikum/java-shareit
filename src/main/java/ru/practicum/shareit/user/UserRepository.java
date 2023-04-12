package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<UserDto> getUsers();

    User create(User user) throws UserAlreadyExistsException;

    void deleteUser(Long id);

}
