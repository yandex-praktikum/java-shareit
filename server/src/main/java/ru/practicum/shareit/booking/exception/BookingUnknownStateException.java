package ru.practicum.shareit.booking.exception;

public class BookingUnknownStateException extends RuntimeException {
    public BookingUnknownStateException(String message) {
        super(message);
    }
}
