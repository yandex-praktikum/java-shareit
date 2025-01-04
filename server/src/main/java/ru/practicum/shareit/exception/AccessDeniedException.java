package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {
    private final int statusCode;

    public AccessDeniedException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
