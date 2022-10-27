package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.userDTO.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();

    UserDTO getById(Long id);

    UserDTO create(User user);

    UserDTO update(User user, Long id);

    void delete(Long id);
}