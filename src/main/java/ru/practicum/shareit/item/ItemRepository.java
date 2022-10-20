package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemRepository {

    List<Item> findByUserId(long ownerId);

    Item save(Item item);

    void deleteByUserIdAndItemId(long ownerId, long itemId);

    Item updateItem(long userId, ItemDto itemDto, long itemId);

    Item findByUserIdAndItemId(long userId, long itemId);

    List<Item> getSearchedItems(String text);
}