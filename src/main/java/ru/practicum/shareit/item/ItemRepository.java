package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    void create(Item item);

    void update(Item item);

    Optional<Item> get(Long id);

    Collection<Item> findAll();

    Collection<Item> findAll(Long userId);

    Collection<Item> findAll(String text);
}
