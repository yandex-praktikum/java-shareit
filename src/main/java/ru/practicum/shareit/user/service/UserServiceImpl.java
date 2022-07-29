package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Класс, ответственный за операции с пользователями
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Возвращает список всех пользователей
     */
    @Override
    public List<UserDto> getUserAll() {
        return userStorage.getUserAll();
    }

    /**
     * Валидирует id и возвращает пользователя по ID
     */
    @Override
    public UserDto getUserById(long userId) {
        validationId(userId);

        return userStorage.getUserById(userId);
    }

    /**
     * Добавляет пользователя
     */
    @Override
    public UserDto add(UserDto userDto) {
        return userStorage.add(userDto);
    }

    /**
     * Валидирует id и обновляет пользователя
     */
    @Override
    public UserDto update(long userId, UserDto userDto) {
        validationId(userId);

        UserDto userDtoExisting = getUserById(userId);
        return userStorage.update(userId, userDtoExisting, userDto);
    }

    /**
     * Удаление пользователя
     */
    @Override
    public void delete(long userId) {
        validationId(userId);

        userStorage.delete(userId);
    }

    private void validationId(long id) {
        try {
            if (id <= 0) {
                throw new ValidationException("id должен быть больше 0");
            }
        } catch (ValidationException e) {
            throw e;
        }
    }
}