package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

public interface UserService {
    void delete(Long id);

    List<UserDto> findAll();

    UserDto update(Long id, UserDto userDto);

    UserDto getById(Long id);

    UserDto create(UserDto userDto);
}