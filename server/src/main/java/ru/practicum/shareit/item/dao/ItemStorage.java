package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getItems(Integer ownerId);

    List<Item> getItems(String text);

    Item getItem(Integer id);

    Item addItem(Integer ownerId, Item item);

    Item updateUser(Integer ownerId, Integer id, Item item);
}
