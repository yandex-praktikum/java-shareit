package ru.practicum.shareit.exception;

public class AccessException extends RuntimeException{
    private final String message;

    public AccessException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
