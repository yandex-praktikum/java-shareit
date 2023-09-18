package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

public class UserValidator {
    // Метод, проверяющий, что все обязательные поля UserDto не null
    public static void checkAllFields(UserDto userDto) {
        if (userDto.getName() == null || userDto.getName().isBlank())
            throw new ValidationException("Имя пользователя не может быть пустым");
        if (userDto.getEmail() == null)
            throw new ValidationException("Почта пользователя не может быть пустой");
    }

    // Метод, проверяющий, что введенные поля UserDto валидны (для запросов на обновление пользователя)
    public static void checkNotNullFields(UserDto userDto) {
        if (userDto.getName() != null && userDto.getName().isBlank())
            throw new ValidationException("Имя пользователя не может быть пустым");
        // Поле email и так проверяет аннотацией @Email
    }
}
