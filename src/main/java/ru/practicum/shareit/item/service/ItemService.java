package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * Интерфейс для сервиса вещей
 */
public interface ItemService {
    List<ItemDto> getItems(long userId);

    ItemDto getItemById(long userId, long itemId);

    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    void deleteItem(long userId, long itemId);

    List<ItemDto> searchItemByText(String text);
}
