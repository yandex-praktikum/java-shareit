package ru.practicum.shareit.user;

import java.util.List;

interface UserService {
    List<UserDTO> getAll();

    UserDTO get(long id);

    UserDTO create(User user);

    UserDTO update(User user, long userId);

    UserDTO delete(long userId);
}