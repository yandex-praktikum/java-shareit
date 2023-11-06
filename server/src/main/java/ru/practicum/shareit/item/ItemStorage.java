package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item create(Item item);

    Item findItemById(Long id);

    Item update(Item item);

    List<Item> getAll();

}
