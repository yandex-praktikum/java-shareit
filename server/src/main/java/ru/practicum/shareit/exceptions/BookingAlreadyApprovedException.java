package ru.practicum.shareit.exceptions;

public class BookingAlreadyApprovedException extends RuntimeException {
    public BookingAlreadyApprovedException(String message) {
        super(message);
    }
}