package ru.practicum.shareit.exception.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundRecordInBD;
import ru.practicum.shareit.exception.ValidateException;

@RestControllerAdvice
@Slf4j
@Getter
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleForBadRequest(final ValidateException ex) {
        String error = "Error 400. Bad Request.";
        String message = ex.getMessage();
        log.error(error + " — " + message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error + " — " + message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleForNotFound(final NotFoundRecordInBD ex) {
        String error = "Error 404. Not Found.";
        String message = ex.getMessage();
        log.error(error + " — " + message);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error + " — " + message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<?> handleForConflict(final ConflictException ex) {
        String error = "Error 409. Conflict in program.";
        String message = ex.getMessage();
        log.error(error + " — " + message);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error + " — " + message);

    }
}
