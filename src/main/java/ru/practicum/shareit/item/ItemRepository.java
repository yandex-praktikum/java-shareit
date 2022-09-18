package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemRepository {
    List<Item> findUserItems(Long userId);

    List<Item> findItemsByText(String text);

    Item find(Long id);

    Item add(Item item);

    Item update(Long id, Item item);

    void delete(Long id);
}