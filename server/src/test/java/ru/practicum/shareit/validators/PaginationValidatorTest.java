package ru.practicum.shareit.validators;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.InvalidPaginationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaginationValidatorTest {
    @Test
    void validatePaginationNormalWay() {
        Pageable page = PaginationValidator.validate(20, 20);
        assertEquals(1, page.getPageNumber());
        assertEquals(20, page.getPageSize());
    }

    @Test
    void validatePaginationFromErrorTest() {
        final InvalidPaginationException exception = assertThrows(
                InvalidPaginationException.class,
                () -> PaginationValidator.validate(-1, 20));

        assertEquals("Неверный параметр from", exception.getMessage());
    }

    @Test
    void validatePaginationSizeErrorTest() {
        final InvalidPaginationException exception = assertThrows(
                InvalidPaginationException.class,
                () -> PaginationValidator.validate(20, 0));

        assertEquals("Неверный параметр size", exception.getMessage());
    }
}