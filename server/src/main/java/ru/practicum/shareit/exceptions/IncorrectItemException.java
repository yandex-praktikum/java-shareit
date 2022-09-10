package ru.practicum.shareit.exceptions;

public class IncorrectItemException extends RuntimeException {
    public IncorrectItemException(String message) {
        super(message);
    }
}
