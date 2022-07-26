package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    void deleteItem(int itemId);

    List<Item> getAllItems();

    List<Item> findItemByUserId(int userId);

    Item getItemById(int itemId);
}
