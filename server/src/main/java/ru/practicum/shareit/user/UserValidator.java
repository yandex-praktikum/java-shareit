package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

@Slf4j
public class UserValidator {

    public static boolean validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Адрес электронной почты не может быть пустым.");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Введен неверный адрес электронной почты.");
            throw new ValidationException("Введен неверный адрес электронной почты.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не может быть пустым.");
            throw new ValidationException("Имя пользователя не может быть пустым.");
        }
        return true;
    }
}
