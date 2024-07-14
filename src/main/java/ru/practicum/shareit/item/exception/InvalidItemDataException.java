package ru.practicum.shareit.item.exception;

public class InvalidItemDataException extends RuntimeException {
    public InvalidItemDataException(String message) {
        super(message);
    }
}