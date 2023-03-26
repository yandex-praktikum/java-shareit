package ru.practicum.shareit.exceptions;

public class ItemAccessDeniedException extends RuntimeException {
    public ItemAccessDeniedException(String message) {
        super(message);
    }
}