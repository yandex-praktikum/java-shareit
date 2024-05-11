package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.IncorrectBookingTimeException;
import ru.practicum.shareit.booking.exceptions.NotOwnerAndNotBookerException;
import ru.practicum.shareit.item.exception.IncorrectUserException;
import ru.practicum.shareit.item.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.exceptions.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ItemIsNotAvailableException.class, ValidationException.class, IncorrectBookingTimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        Class<?> type = e.getRequiredType();
        String message;
        if (type.isEnum()) {
            message = "Unknown state: UNSUPPORTED_STATUS";
            return new ErrorResponse(message);
        } else {
            return new ErrorResponse(e.getMessage());
        }
    }

    @ExceptionHandler({UserNotFoundException.class, ItemNotFoundException.class, BookingNotFoundException.class, NotOwnerAndNotBookerException.class, RequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IncorrectUserException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIncorrectUserException(final IncorrectUserException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }
}
