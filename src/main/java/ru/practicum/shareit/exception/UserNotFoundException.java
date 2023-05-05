package ru.practicum.shareit.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String s) {
        super((s));
    }

    public UserNotFoundException() {
    }
}
