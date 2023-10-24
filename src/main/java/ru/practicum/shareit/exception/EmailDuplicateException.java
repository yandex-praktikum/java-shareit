package ru.practicum.shareit.exception;

public class EmailDuplicateException extends RuntimeException {

    public EmailDuplicateException(String message) {
        super(message);
    }
}
