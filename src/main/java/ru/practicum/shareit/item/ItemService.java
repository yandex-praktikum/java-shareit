package ru.practicum.shareit.item;

import java.util.List;
import java.util.Optional;

public interface ItemService<T> {
    T create(T data);

    T update(T data);

    Optional<T> get(Long id);

    List<T> getAll();
}
