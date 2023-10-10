package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    private static final String NOT_FOUND_EXCEPTION_MESSAGE = "%s with id %s not found";

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, long id) {
        super(String.format(NOT_FOUND_EXCEPTION_MESSAGE, message, id));
    }
}
