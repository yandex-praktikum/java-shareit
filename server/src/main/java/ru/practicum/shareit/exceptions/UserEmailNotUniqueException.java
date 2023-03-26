package ru.practicum.shareit.exceptions;

public class UserEmailNotUniqueException extends RuntimeException {
    public UserEmailNotUniqueException(String message) {
        super(message);
    }
}