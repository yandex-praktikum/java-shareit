package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorException exceptionHandler(ValidateException validateException) {
        log.debug(validateException.getMessage());
        return new ErrorException(System.currentTimeMillis(), validateException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorException exceptionHandler(ExeptionNotFound exeptionNotFound) {
        log.debug(exeptionNotFound.getMessage());
        return new ErrorException(System.currentTimeMillis(), exeptionNotFound.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorException exceptionHandler(NotFoundException notFoundException) {
        log.debug(notFoundException.getMessage());
        return new ErrorException(System.currentTimeMillis(), notFoundException.getMessage());
    }
}
