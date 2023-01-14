package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {
    Item get(Long id);
    Collection<Item> getAllByOwnerId(Long ownerId);
    Item add(Item item);
    Item update(Item item);
    Boolean delete(Long id);
    Collection<Item> search(String keyword, Long userId);
}
