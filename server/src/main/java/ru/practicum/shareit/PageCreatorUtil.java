package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.exception.PaginationException;

public class PageCreatorUtil {

    public static PageRequest createPage(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new PaginationException("Параметры пагинации заданы неверно");
        }
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}
