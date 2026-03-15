package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> catchAllExceptions(Exception ex) {
        log.error("500: ", ex);
        return Map.of("error", "Произошла ошибка");
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> catchValidationErrors(Exception ex) {
        log.error("400: {}", ex.getMessage());
        return Map.of("error", "Ошибка валидации");
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> catchConflictStatus(ConflictException ex) {
        log.error("409: {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> catchNotFoundStatus(NotFoundException ex) {
        log.error("404: {}", ex.getMessage());
        return Map.of("error", ex.getMessage());
    }
}