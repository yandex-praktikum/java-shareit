package ru.practicum.shareit.item;

import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(int userId, ItemDto itemDto) throws ValidationException;

    ItemDto updateItem(int userId, int itemId, ItemDto itemDto) throws ValidationException;

    ItemDto findItemById(Integer itemId) throws ValidationException;

    List<ItemDto> findAllItemsForUser(int userId) throws ValidationException;

    List<ItemDto> searchItemsByText(String text);
}
