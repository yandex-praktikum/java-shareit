package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<UserDto> findAllUsers();

    User create(User user);

    void deleteUser(Long id);

    User findUserById(Long userId);

}
