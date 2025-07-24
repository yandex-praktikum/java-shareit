package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long itemId, Item item);

    Item getItemById(Long itemId);

    List<Item> getAllItemsByOwner(Long userId);

    List<Item> searchItems(String text);
}