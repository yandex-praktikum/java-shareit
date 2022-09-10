package ru.practicum.shareit.exceptions;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException() {
        super();
    }

    public RequestNotFoundException(String message) {
        super(message);
    }
}
