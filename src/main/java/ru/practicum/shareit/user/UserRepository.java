package ru.practicum.shareit.user;

import java.util.List;

interface UserRepository {
    List<UserDTO> getAll();

    UserDTO get(long userId);

    UserDTO create(User user);

    UserDTO update(User user, long userId);

    UserDTO delete(long userId);

}