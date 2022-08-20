package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    UserDto getUser(Long id);

    void delete(Long id);

    List<UserDto> getAll();

}
