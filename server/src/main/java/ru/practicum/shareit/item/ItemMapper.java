package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getOwner().getId(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemDtoForRequest toItemDtoForRequest(Item item) {
        return new ItemDtoForRequest(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable(),
                item.getRequest().getId()
        );
    }

    public static Item toItem(ItemDto itemDto, User user) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user
        );
    }

    public static Item toItem(ItemDto itemDto, User user, ItemRequest itemRequest) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                user,
                itemRequest
        );
    }

    public static List<ItemDto> listToItemDto(Iterable<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDto(item));
        }
        return dtos;
    }

    public static List<ItemDtoForRequest> listToItemDtoForRequest(Iterable<Item> items) {
        List<ItemDtoForRequest> dtos = new ArrayList<>();
        for (Item item : items) {
            dtos.add(toItemDtoForRequest(item));
        }
        return dtos;
    }
}
