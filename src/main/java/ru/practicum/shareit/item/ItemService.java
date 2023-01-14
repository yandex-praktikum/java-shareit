package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto get(Long id);
    Collection<ItemDto> getAllByUserId(Long userId);
    ItemDto add(ItemDto itemDto, Long ownerId);
    ItemDto update(ItemDto itemDto, Long itemId, Long userId);
    Boolean delete(Long id);
    Collection<ItemDto> search(String keyword, Long userId);
}