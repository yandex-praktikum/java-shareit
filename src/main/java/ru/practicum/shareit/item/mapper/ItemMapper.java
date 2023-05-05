package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto itemToItemDto(Item item, List<CommentDto> comments) {
        if (item.getItemRequest() == null) {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .requestId(null)
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(comments)
                    .build();
        } else {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .requestId(item.getItemRequest().getId())
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(comments)
                    .build();
        }
    }

    public static ItemDto itemToItemDto(Item item) {
        ItemDto itemDto;
        try {
            itemDto = ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .requestId(item.getItemRequest().getId())
                    .build();
        } catch (NullPointerException e) {
            itemDto = ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .requestId(null)
                    .build();
        }
        return itemDto;
    }

    public static ItemDto itemToItemDtoWithBookings(Item item, BookingDto lastBooking, BookingDto nextBooking,
                                                    List<CommentDto> comments) {
        if (item.getItemRequest() == null) {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .requestId(null)
                    .lastBooking(lastBooking)
                    .nextBooking(nextBooking)
                    .comments(comments)
                    .build();
        } else {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .requestId(item.getItemRequest().getId())
                    .lastBooking(lastBooking)
                    .nextBooking(nextBooking)
                    .comments(comments)
                    .build();
        }
    }

    public static Item itemDtoToItem(ItemDto itemDto, User user, ItemRequest request) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .itemRequest(request)
                .build();
    }

    public static List<ItemDto> itemsToItemDtos (List<Item> items) {
        List<ItemDto> itemDtos = new ArrayList<>();
        for (Item item : items) {
            itemDtos.add(itemToItemDto(item));
        }
        return itemDtos;
    }
}