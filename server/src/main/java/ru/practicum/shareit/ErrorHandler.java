package ru.practicum.shareit;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.BookingUnknownStateException;
import ru.practicum.shareit.booking.exception.IncorrectBookingParameterException;
import ru.practicum.shareit.item.exception.IncorrectItemParameterException;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.user.exceptions.IncorrectUserParameterException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIncorrectParameterException(final IncorrectParameterException incorrectParameterException) {
        return new ErrorResponse(incorrectParameterException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectUserParameterException(final IncorrectUserParameterException incorrectUserParameterException) {
        return new ErrorResponse(incorrectUserParameterException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectUserParameterException(final DataIntegrityViolationException dataIntegrityViolationException) {
        return new ErrorResponse(dataIntegrityViolationException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final IncorrectItemParameterException e) {

        return new ErrorResponse("Unknown state: " + e.getParameter());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIncorrectStateException(final BookingUnknownStateException e) {

        return new ErrorResponse("Unknown state: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectParameterException(final IncorrectBookingParameterException incorrectBookingParameterException) {
        return new ErrorResponse(incorrectBookingParameterException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException userNotFoundException) {
        return new ErrorResponse("Нет пользователя с таким ID");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final BookingNotFoundException bookingNotFoundException) {
        return new ErrorResponse("Нет пользователя с таким ID={}");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoBookingStatusException(final MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        return new ErrorResponse("Unknown state: " + methodArgumentTypeMismatchException.getValue());
    }
}
