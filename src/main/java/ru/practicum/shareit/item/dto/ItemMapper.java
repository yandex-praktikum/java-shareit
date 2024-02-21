package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.exception.ValidationException;

public final class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestId() != null ? item.getRequestId() : null
        );
    }

    public static Item toItem(ItemDto itemDto, int ownerId) {
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            throw new ValidationException("Поле name должно быть заполнено.");
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ValidationException("Поле description должно быть заполнено.");
        }
        if (itemDto.getAvailable() == null) {
            throw new ValidationException("Поле available должно быть заполнено.");
        }
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId,
                itemDto.getRequestId());
    }
}
