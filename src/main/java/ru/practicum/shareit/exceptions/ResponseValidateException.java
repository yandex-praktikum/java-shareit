package ru.practicum.shareit.exceptions;

public class ResponseValidateException extends RuntimeException {
    public ResponseValidateException(String message) {
        super(message);
    }
}
