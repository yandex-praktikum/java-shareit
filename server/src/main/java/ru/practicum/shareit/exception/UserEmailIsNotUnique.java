package ru.practicum.shareit.exception;

public class UserEmailIsNotUnique extends RuntimeException {
    public UserEmailIsNotUnique(String message) {
        super(message);
    }
}
