package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<ItemDto> findByUserId(long userId);

    ItemDto save(long userId, ItemDto itemDto);

    List<Item> getListItems();

    ItemDto getItemById(long itemId);

    void deleteByUserIdAndItemId(long userId, long itemId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws NotFoundException;

    List<ItemDto> searchItem(String search);
}