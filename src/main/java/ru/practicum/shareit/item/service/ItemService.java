package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Item saveItem(long userId, ItemDto itemDto);

    void deleteItem(long userId, long id);

    Item updateItem(long userId, long itemId, ItemDto itemDto);

    Item getItemByItemId(long id);

    Collection<Item> getAllItemByUserId(long id);

    Collection<Item> findItemByName(String name);
}
