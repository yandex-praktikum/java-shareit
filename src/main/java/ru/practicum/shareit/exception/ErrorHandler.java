package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    public ErrorResponse incorrectParameter(final ValidationException e) {
        return new ErrorResponse("Error", String.format("Ошибка “%s”", e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponse incorrectParameter(final NotFoundException e) {
        return new ErrorResponse("Error", String.format("Ошибка “%s”", e.getMessage()));
    }

    @ExceptionHandler({ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT) //409
    public ErrorResponse incorrectParameter(final ConflictException e) {
        return new ErrorResponse("Error", String.format("Ошибка “%s”", e.getMessage()));
    }

    @ExceptionHandler({EmptyListException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponse incorrectParameter(final RuntimeException e) {
        return new ErrorResponse("Error", String.format("Ошибка “%s”", e.getMessage()));
    }

}

class ErrorResponse {
    private final String error;
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
