package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item toItem(ItemDto dto) {
        return Item.builder()
                .available(dto.getAvailable())
                .description(dto.getDescription())
                .name(dto.getName())
                .id(dto.getId())
                .build();
    }

    public static ItemDto toItemDto(Item entity) {
        return ItemDto.builder()
                .requestId(entity.getRequestId())
                .available(entity.getAvailable())
                .description(entity.getDescription())
                .name(entity.getName())
                .id(entity.getId())
                .build();
    }
}