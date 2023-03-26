package ru.practicum.shareit.validators;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exceptions.InvalidPaginationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationValidator {
    public static Pageable validate(int from, int size) {
        if (from < 0) {
            throw new InvalidPaginationException("Неверный параметр from");
        }
        if (size <= 0) {
            throw new InvalidPaginationException("Неверный параметр size");
        }

        int pageNumber = from / size;
        return PageRequest.of(pageNumber, size);
    }
}
