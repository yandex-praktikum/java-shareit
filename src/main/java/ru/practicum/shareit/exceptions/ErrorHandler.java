package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.InvalidParameterException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    private void handle(final DuplicateEmailException e){
        log.error("Ошибка " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void handle(final WrongParameterException e){log.error("Ошибка " + e.getMessage());}
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void handle(final NotFoundException e){log.error("Ошибка " + e.getMessage());}
}
