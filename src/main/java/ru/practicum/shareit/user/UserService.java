package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEmailDto;
import ru.practicum.shareit.user.dto.UserNameDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(UserDto userDto);
    Optional<User> update (Long id, UserDto userDto);
    Optional<User> updateName(Long id, UserNameDto dto);
    Optional<User> updateEmail(Long id, UserEmailDto dto);
    Optional<User> getUserById(Long id);
    List<User> getAll();
    void deleteUserById(Long id);
}
