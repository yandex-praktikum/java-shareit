package ru.practicum.shareit.exception;

public class RightsException extends RuntimeException {
    public RightsException() {
        super("not enough rights");
    }
}
