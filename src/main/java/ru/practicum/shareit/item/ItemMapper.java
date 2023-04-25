package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public static Item toItemModel(ItemDto itemDto, Long ownerId) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), ownerId);
    }

    public static List<ItemDto> toItemDtoList(List<Item> items) {
        List<ItemDto> result = new ArrayList<>();
        for (Item item : items) {
            ItemDto itemDto = toItemDto(item);
            result.add(itemDto);
        }
        return result;
    }
}