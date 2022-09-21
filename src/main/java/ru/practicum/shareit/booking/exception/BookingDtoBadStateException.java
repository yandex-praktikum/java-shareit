package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
        reason = "Unknown state: UNSUPPORTED_STATUS")
public class BookingDtoBadStateException extends RuntimeException {
    public BookingDtoBadStateException(String message) {
        super(message);
    }
}