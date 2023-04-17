package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto update(ItemDto inputItemDto, Long ownerId, Long itemId);

    ItemDto create(ItemDto inputItemDto, Long ownerId);

    ItemDto findItemById(Long itemId);

    List<ItemDto> findAllItemsOwner(Long ownerId);

    List<ItemDto> searchItem(String text);
}



