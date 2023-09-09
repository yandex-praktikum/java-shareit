package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    UserDto create(UserDto userDto);

    UserDto get(Long id);

    List<UserDto> getAll();

    UserDto update(UserDto userDto, long id);

    void delete(Long id);
}
