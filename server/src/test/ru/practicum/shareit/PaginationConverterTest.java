package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.exception.ValidationException;
import ru.practicum.utilities.PaginationConverter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaginationConverterTest {

    @Test
    void convertTest() {
            PaginationConverter paginationConverter = new PaginationConverter();

            assertThrows(ValidationException.class, () -> paginationConverter.convert(0, -1, null));

            assertNotNull(paginationConverter.convert(0, 2, null));

            assertNotNull(paginationConverter.convert(null, null, null));
    }
}
