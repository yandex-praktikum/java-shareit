package ru.practicum.shareit.error_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class,
            BookingItemUnavailableExceprion.class,
            BookingAlreadyApprovedException.class,
            CommentAuthorHaveNoBookingsException.class,
            InvalidPaginationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingUnsupportedStatusException(final BookingUnsupportedStatusException e) {
        log.info(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getMessage());
    }

    @ExceptionHandler({ItemAccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleItemAccessDeniedException(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class,
            ItemNotFoundException.class,
            BookingNotFoundException.class,
            ItemRequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserEmailNotUniqueException(final UserEmailNotUniqueException e) {
        log.info(e.getMessage());
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler({NullPointerException.class,
            IllegalArgumentException.class,
            EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse serverExceptionHandler(RuntimeException e) {
        log.info(e.getMessage(), e);
        return new ErrorResponse("error", "Внутренняя ошибка сервера");
    }
}