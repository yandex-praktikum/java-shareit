package ru.practicum.shareit.exception;

public class BadRequestException extends Exception {
    public BadRequestException() {
    }

    public BadRequestException(final String message) {
        super(message);
    }

}
