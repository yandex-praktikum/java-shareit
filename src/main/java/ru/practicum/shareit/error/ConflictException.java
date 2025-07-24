package ru.practicum.shareit.error;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}