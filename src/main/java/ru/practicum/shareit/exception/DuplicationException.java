package ru.practicum.shareit.exception;

public class DuplicationException extends RuntimeException {
    public DuplicationException() {
        super();
    }

    public DuplicationException(String message) {
        super(message);
    }
}
