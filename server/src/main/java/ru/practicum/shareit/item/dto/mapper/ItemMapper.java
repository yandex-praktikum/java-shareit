package ru.practicum.shareit.item.dto.mapper;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.RequestItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.util.enums.ItemStatus;

import java.util.Collection;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                fromItemStatusToBoolean(item.getStatus()),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDto toItemDto(Item item, Collection<CommentDto> comments) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                fromItemStatusToBoolean(item.getStatus()),
                item.getRequest() != null ? item.getRequest().getId() : null,
                comments
        );
    }

    public static Item fromDto(RequestItemDto itemDto) {
        return new Item(
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                fromBooleanToItemStatus(itemDto.getAvailable()),
                null,
                null);
    }

    public static Item fromDto(Long itemId, RequestItemDto itemDto) {
        return new Item(
                itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                fromBooleanToItemStatus(itemDto.getAvailable()),
                null,
                null);
    }

    private static ItemStatus fromBooleanToItemStatus(Boolean condition) {
        if (condition == null)
            return null;
        return condition ? ItemStatus.AVAILABLE : ItemStatus.UNAVAILABLE;
    }

    private static Boolean fromItemStatusToBoolean(ItemStatus itemStatus) {
        if (itemStatus == null)
            return null;
        return itemStatus == ItemStatus.AVAILABLE;
    }
}
