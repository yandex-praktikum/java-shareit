package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }
}
