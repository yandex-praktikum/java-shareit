package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {

    List<UserDto> getAll();

    @Transactional
    UserDto create(UserDto userDto);

    @Transactional
    UserDto update(UserUpdateDto userUpdateDto, Long userId);

    @Transactional
    void deleteById(Long id);

    User getUserByIdOrThrow(Long userId);
}
