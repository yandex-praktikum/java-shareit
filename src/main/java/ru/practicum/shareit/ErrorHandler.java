package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingDtoBadStateException;
import ru.practicum.shareit.booking.exception.BookingNotChangeStatusException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.ErrorResponse;
import ru.practicum.shareit.item.exception.ItemNotAvalibleException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserIsNotBookerException;
import ru.practicum.shareit.item.exception.UserIsNotOwnerException;
import ru.practicum.shareit.item.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.request.exception.ItemRequestNotGoodParametrsException;
import ru.practicum.shareit.user.exception.NullEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class,
            ItemRequestNotFoundException.class, ItemNotFoundException.class, UserIsNotOwnerException.class,
            BookingNotFoundException.class})
    public ErrorResponse handleNotFoundException(final Exception e) {
        return new ErrorResponse(
                String.format("\"%s\"", e.getMessage())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NullEmailException.class, ItemRequestNotGoodParametrsException.class,
            ItemNotAvalibleException.class, UserIsNotBookerException.class, BookingDtoBadStateException.class,
            BookingNotChangeStatusException.class})
    public ErrorResponse handleBadRequestException(final Exception e) {
        return new ErrorResponse(
                String.format("\"%s\"", e.getMessage())
        );
    }
}
