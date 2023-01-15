package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Collection<User> findAll();

    Optional<User> getById(Long id);

    UserDto create(UserDto userDto);

    User update(Long id, User user);

    void delete(Long id);
}
