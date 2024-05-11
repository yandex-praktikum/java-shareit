package ru.practicum.shareit.booking.exceptions;

public class IncorrectUserException extends RuntimeException {
    public IncorrectUserException(String message) {
        super(message);
    }
}