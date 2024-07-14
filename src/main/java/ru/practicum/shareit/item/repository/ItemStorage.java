package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item save(Item item);

    Item findById(Long itemId);

    List<Item> findByOwnerId(Long ownerId);

    List<Item> search(String text);
}