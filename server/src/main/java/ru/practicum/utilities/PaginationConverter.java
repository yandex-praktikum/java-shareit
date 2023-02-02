package ru.practicum.utilities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.exception.ValidationException;


@Component
public class PaginationConverter {

    public Pageable convert(Integer from, Integer size, String sortBy) {
        Pageable pageable;
        if (from == null && size == null) {
            pageable = Pageable.unpaged();
        } else if (checkPagination(from, size)) {
            pageable = sortBy != null ? PageRequest.of(from, 1, Sort.by(sortBy).descending())
                    : PageRequest.of(from, 1);
        } else {
            throw new ValidationException("incorrect pagination parameters");
        }
        return pageable;
    }

    private boolean checkPagination(Integer from, Integer size) {
        return from >= 0 && size > 0;
    }
}