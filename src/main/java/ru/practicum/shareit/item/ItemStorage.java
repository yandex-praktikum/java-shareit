package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    ItemDto addItem(int userId, ItemDto itemDto);

    ItemDto updateItem(int userId, int itemId, ItemDto itemDto);

    Item findItemById(Integer itemId);

    ItemDto findItemDtoById(Integer itemId);

    List<ItemDto> findAllItemsForUser(int userId);

    List<Item> findAllItems();
}
