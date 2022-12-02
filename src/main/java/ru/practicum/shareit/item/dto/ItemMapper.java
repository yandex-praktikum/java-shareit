package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto itemToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item itemFromItemDto(ItemDto itemDto, Integer userId, Integer itemId) {
        return new Item(itemId, itemDto.getName(), itemDto.getDescription(), userId, itemDto.getAvailable());
    }
}
