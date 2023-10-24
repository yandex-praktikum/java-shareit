package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemDao {
    ItemDto addItem(int userId, Item item);

    ItemDto updateItem(int userId, int itemId, Map<Object, Object> fields);

    Item getItemById(int itemId);

    ItemDto getItemDtoById(int itemId);

    List<ItemDto> getAllItemForOwner(int ownerId);

    List<ItemDto> searchItem(String request);
}
