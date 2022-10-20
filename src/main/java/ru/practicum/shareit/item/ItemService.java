package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

interface ItemService {
    Item addNewItem(Long userId, Item item);

    Item updateItem(Long userId, ItemDto itemDto, Long itemId);

    List<Item> getItems(long userId);

    void deleteItem(long userId, long itemId);

    Item getItem(long userId, long itemId);

    List<Item> getSearchedItems(String text);
}