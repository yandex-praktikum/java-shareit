package ru.practicum.user;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getById(Long id);

    User create(UserDto user);

    User put(UserDto user, Long id);

    boolean delete(Long userId);

    List<UserDto> getAll();

}
