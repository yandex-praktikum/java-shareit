package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Интерфейс для хранения объектов пользователей
 */
@Component
public interface UserStorage {
    List<UserDto> getUserAll();

    UserDto getUserById(long id);

    UserDto add(UserDto userDto);

    UserDto update(long id, UserDto userDtoExisting, UserDto userDto);

    void delete(long id);
}
