package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemDto addItem(int userId, Item item);

    ItemDto updateItem(int userId, int itemId, Map<Object, Object> fields);

    ItemDto getItemDtoById(int itemId);

    List<ItemDto> getAllItemForOwner(int ownerId);

    List<ItemDto> searchItem(String request);
}
