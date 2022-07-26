package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addNewItem(int userId, Item item);

    void deleteItem(int userId, int itemId);

    List<Item> getItemsByUserId(int userId);

    List<Item> getAllItems();

    Item updateItem(int userId, int itemId, Item item);

    Item getItemById(int itemId);

    List<Item> findItemByRequest(String text);
}
