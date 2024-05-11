package ru.practicum.shareit.booking.exceptions;

public class IncorrectBookingTimeException extends RuntimeException {
    public IncorrectBookingTimeException(String message) {
        super(message);
    }
}
