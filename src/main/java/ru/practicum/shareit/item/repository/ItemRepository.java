package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    public Item create(Item item);

    void update(Item item);
    Item getById(Long itemID);

    List<Item> getAll();
}
