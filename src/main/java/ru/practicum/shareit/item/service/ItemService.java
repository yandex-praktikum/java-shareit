package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long ownerId, ItemDto itemDto);

    ItemDto updateItem(Long itemId, Long ownerId, ItemDto itemDto);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsByOwner(Long ownerId);

    List<ItemDto> searchItems(String text);

}
