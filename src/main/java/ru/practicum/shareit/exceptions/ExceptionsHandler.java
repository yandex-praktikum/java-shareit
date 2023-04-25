package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(final ValidationException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                String.format(e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse objectNotFoundException(final ObjectNotFoundException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                String.format(e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeException(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                String.format(e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse emailConflictException(final EmailConflictException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                String.format(e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse wrongUserException(final WrongUserException e) {
        log.error(e.getMessage());
        return new ErrorResponse(
                String.format(e.getMessage())
        );
    }
}