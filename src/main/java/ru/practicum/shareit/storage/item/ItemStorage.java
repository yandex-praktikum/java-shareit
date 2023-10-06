package ru.practicum.shareit.storage.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item addItem(Item item);

    Item updateItem(Item item);

    Item getItemById(int id);

    List<Item> getAllItemsByOwnerId(int id);

    List<Item> searchItem(String keyWord);
}
