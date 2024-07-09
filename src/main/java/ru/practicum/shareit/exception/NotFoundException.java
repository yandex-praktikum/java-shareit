package ru.practicum.shareit.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long message) {
        super("Not found id: " + message);
    }
}
