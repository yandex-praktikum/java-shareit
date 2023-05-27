package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getIsAvailable(), item.getRequestId(), null, null, null);
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), itemDto.getRequestId(), null);
    }

    public static List<ItemDto> toItemDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
