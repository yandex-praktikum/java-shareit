package ru.practicum.shareit.exception;

public class EmptyListException extends RuntimeException {
    private final String message;

    public EmptyListException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
