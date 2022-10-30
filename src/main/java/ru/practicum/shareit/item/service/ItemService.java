package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.itemDto.ItemDto;

public interface ItemService {
    public ItemDto createItem(ItemDto itemDto, Long userId);
}
