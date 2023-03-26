package ru.practicum.shareit.exceptions;

public class BookingUnsupportedStatusException extends RuntimeException {
    public BookingUnsupportedStatusException(String message) {
        super(message);
    }
}