package ru.practicum.shareit.exception;

public class NoSuchItemException extends RuntimeException {
    public NoSuchItemException() {
        super();
    }

    public NoSuchItemException(String message) {
        super(message);
    }
}
