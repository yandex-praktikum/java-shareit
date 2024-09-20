package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

interface ItemService {
    ItemDto addItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, ItemDto itemDto, Long itemId);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> getItemsFromUsers(Long userId);

    List<ItemDto> search(String search, Long userId);
}
