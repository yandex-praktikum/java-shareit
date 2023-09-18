package ru.practicum.shareit.exception;

public class NoAccessException extends RuntimeException {
    public NoAccessException(String message) {
        super(message);
    }
}
