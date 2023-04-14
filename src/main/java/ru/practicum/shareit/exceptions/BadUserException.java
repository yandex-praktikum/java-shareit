package ru.practicum.shareit.exceptions;

public class BadUserException extends  RuntimeException {
    public BadUserException(String message) {
        super("error: " + message);
    }
}
