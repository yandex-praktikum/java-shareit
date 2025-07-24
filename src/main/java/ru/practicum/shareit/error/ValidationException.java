package ru.practicum.shareit.error;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}