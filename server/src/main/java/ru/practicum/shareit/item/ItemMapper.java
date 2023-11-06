package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingToItemDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingDates;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toFullItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwnerId(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDtoWithBookingDates toItemDtoWithBookingDates(
            Item item, BookingToItemDto lastBooking, BookingToItemDto nextBooking, List<CommentFullDto> comment) {
        return new ItemDtoWithBookingDates(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwnerId(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                lastBooking,
                nextBooking,
                comment
        );
    }

    public static Item fromItemDto(ItemDto itemDto, boolean available, Long ownerId, ItemRequest itemRequest) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                available,
                ownerId,
                itemRequest
        );
    }

    public static Item fromItemUpdateDto(ItemUpdateDto itemDto, boolean isTrue, Long ownerId, Long itemId) {
        return new Item(
                itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                isTrue,
                ownerId,
                null
        );
    }

    public static List<ItemDto> toItemsDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(toFullItemDto(item));
        }
        return itemsDto;
    }
}
