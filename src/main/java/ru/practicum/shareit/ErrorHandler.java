package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.IncorrectBookingParameterException;
import ru.practicum.shareit.item.exception.IncorrectItemParameterException;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.request.exception.IncorrectPageParametrException;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectPageParameterException(final IncorrectPageParametrException incorrectPageParametrException) {
        return new ErrorResponse(incorrectPageParametrException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIncorrectUserParameterException(final IncorrectUserParameterException incorrectUserParameterException) {
        return new ErrorResponse(incorrectUserParameterException.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectItemParameterException(final IncorrectItemParameterException e) {

        return new ErrorResponse("Unknown state: " + e.getParameter());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectBookingParameterException(final IncorrectBookingParameterException incorrectBookingParameterException) {
        return new ErrorResponse(incorrectBookingParameterException.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException userNotFoundException) {
        return new ErrorResponse("Нет пользователя с таким ID");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFoundException(final BookingNotFoundException bookingNotFoundException) {
        return new ErrorResponse("Нет пользователя с таким ID={}");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoBookingStatusException(final MethodArgumentTypeMismatchException methodArgumentTypeMismatchException) {
        return new ErrorResponse("Unknown state: " + methodArgumentTypeMismatchException.getValue());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemRequestNotFoundException(final ItemRequestNotFoundException itemRequestNotFoundException) {
        return new ErrorResponse(itemRequestNotFoundException.getMessage());
    }
}
