package ru.practicum.shareit.request.exception;

public class PaginationException extends RuntimeException {
    public PaginationException(String message) {
        super(message);
    }
}