package ru.practicum.shareit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("E001", e.getMessage());
    }


    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("E002", e.getMessage());
    }

    @ExceptionHandler({UnavailibleException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnavailableError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("E002", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.info(e.getMessage());
        return new ErrorResponse("E003", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ErrorResponse handleMethodArgumentTypeMismatch(final Throwable e) {
        log.info(e.getMessage());
        return new ErrorResponse("E004", "Unknown state: UNSUPPORTED_STATUS");
    }

    @AllArgsConstructor
    @Getter
    public static class ErrorResponse {
        String errorCode;
        String error;
    }
}

