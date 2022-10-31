package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.itemDto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId);

    ItemDto getByID(Long id);

    List<ItemDto> getAllItems(Long userId);

    List<ItemDto> searchItem(String request);
}
