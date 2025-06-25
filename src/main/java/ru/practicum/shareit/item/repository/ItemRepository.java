package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item save(Item item);

    void delete(long id);

    Item update(Item item);

    Item getByItemId(long id);

    Collection<Item> getAllByUserId(long id);

    Collection<Item> findItemByName(String name);

}
