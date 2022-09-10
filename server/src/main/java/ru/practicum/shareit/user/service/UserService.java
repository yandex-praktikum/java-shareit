package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User getUserById(long userId);

    User update(UserDto dto, long userId);

    Collection<User> getAll();

    User create(User user);

    void delete(long userId);
}
