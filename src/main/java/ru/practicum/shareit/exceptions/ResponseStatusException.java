package ru.practicum.shareit.exceptions;

public class ResponseStatusException extends RuntimeException {
    public ResponseStatusException(String message) {
        super(message);
    }
}
