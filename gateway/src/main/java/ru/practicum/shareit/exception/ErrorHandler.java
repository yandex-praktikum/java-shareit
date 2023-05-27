package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> validationException(final IllegalArgumentException ex) {
        log.error("IllegalArgumentException {}", ex.toString());
        Map<String, String> map = new HashMap<>();
        String message = "Unknown state: UNSUPPORTED_STATUS";
        map.put("error", message);
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<String> constraintViolationException(ConstraintViolationException ex) {
        log.error("constraintViolationException  {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<String> noItemUserException(NoSuchElementException ex) {
        log.error("noItemUserException {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("methodArgumentNotValidException  {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleBookingException(final BookingException e) {
        log.error("Exception! BookingException: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> throwable(Throwable ex) {
        log.error("throwable {}", ex.toString());
        Map<String, String> map = new HashMap<>();
        map.put("error", ex.toString());
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);

    }


}
