package ru.practicum.shareit.exeptions;

public class UserEmailValidationException extends Exception {
    public UserEmailValidationException(String message) {
        super(message);
    }
}
