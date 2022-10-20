package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> findAll();

    User save(User user);

    User getUserById(Long id);

    void removeUser(Long userId);

    User updateUser(UserDto userDto, Long userId);
}
