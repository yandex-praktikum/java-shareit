package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ConflictException extends HttpClientErrorException {

    public ConflictException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
