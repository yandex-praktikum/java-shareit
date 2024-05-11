package ru.practicum.shareit.booking.exceptions;

public class NotOwnerAndNotBookerException extends RuntimeException {
    public NotOwnerAndNotBookerException(String message) {
        super(message);
    }
}
