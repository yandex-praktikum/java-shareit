package ru.practicum.shareit.item.exception;

public class ItemIsNotAvailableException extends RuntimeException {
    public ItemIsNotAvailableException(String message) {
        super(message);
    }
}
