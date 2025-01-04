package ru.practicum.shareit.exception;

public class UserIdMissingException extends RuntimeException {
    public UserIdMissingException(String message) {
        super(message);
    }
}
