package ru.practicum.shareit.exception;

public class NotUniqueEmailException extends RuntimeException{
    public NotUniqueEmailException(String message) {
        super(message);
    }
}
