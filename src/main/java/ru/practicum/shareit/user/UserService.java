package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {
    UserDto addUser(User user);

    UserDto updateUser(long userId, User user);

    UserDto getUserDtoById(long userId);

    User getUserById(long userId);

    void deleteUserById(long userId);
}
