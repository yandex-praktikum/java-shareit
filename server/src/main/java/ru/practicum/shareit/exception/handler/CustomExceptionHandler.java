package ru.practicum.shareit.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.exception.model.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            log.warn("Ошибка валидации поля \"{}\" : {}", fieldName, errorMessage);
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Запрашиваемый ресурс не найден", ex.getMessage());
        loggingErrorResponse(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserIdMissingException.class)
    public ResponseEntity<ErrorResponse> handle(UserIdMissingException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Неверный  запрос", ex.getMessage());
        loggingErrorResponse(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> handle(MissingRequestHeaderException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Неверный  запрос", ex.getMessage());
        loggingErrorResponse(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handle(AccessDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("В доступе отказано", ex.getMessage());
        loggingErrorResponse(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handle(ValidationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Ошибка валидации", ex.getMessage());
        loggingErrorResponse(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserEmailIsNotUnique.class)
    public ResponseEntity<ErrorResponse> handle(UserEmailIsNotUnique ex) {
        ErrorResponse errorResponse = new ErrorResponse("Запрос не может быть выполнен", ex.getMessage());
        loggingErrorResponse(errorResponse);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handle(Throwable ex) {
        ErrorResponse errorResponse = new ErrorResponse("Произошла непредвиденная ошибка", ex.getClass() + " " + ex.getMessage());
        log.trace(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void loggingErrorResponse(ErrorResponse errorResponse) {
        log.warn("Ошибка: {}", errorResponse);
    }
}