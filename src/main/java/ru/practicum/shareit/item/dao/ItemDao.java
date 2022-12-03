package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    Item getItemById(Long itemId);

    List<Item> getAllItems(Long userId);

    Item createItem(Item item);

    Item updateItem(Long itemId, Item item);

    List<Item> searchItem(String searchText);
}
