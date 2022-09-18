package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User addUser(UserDto userDto);

    User updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    User findUser(Long id);

    List<User> findAll();

}
