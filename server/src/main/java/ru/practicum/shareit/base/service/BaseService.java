package ru.practicum.shareit.base.service;

import java.util.Collection;

public interface BaseService<T, I> {
    Collection<T> findAll();

    T findById(I id);

    void deleteById(I id);
}
