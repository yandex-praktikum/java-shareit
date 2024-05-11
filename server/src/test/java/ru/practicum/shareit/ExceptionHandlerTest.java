package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exceptions.ValidationException;


public class ExceptionHandlerTest {

    ErrorResponse errorResponse;

    ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void handleValidationExceptionTest() {
        ValidationException e = new ValidationException("Ошибка валидации");
        errorResponse = errorHandler.handleValidationException(e);
        Assertions.assertEquals("Ошибка валидации", errorResponse.getError());
    }

    @Test
    void handleNotFoundExceptionTest() {
        ItemNotFoundException e = new ItemNotFoundException("Такой вещи не существует");
        errorResponse = errorHandler.handleNotFoundException(e);
        Assertions.assertEquals("Такой вещи не существует", errorResponse.getError());
    }

}
