package ru.practicum.shareit.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingNotChangeStatusException extends RuntimeException {
    public BookingNotChangeStatusException(String message) {
        super(message);
    }
}