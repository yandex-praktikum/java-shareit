package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static Item itemDTOInitem(Item item){
        return new Item(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailabilityStatus(),
                item.getOwnerId(),
                item.getRequestId()
        );
    }
    public static ItemDto itemInDto(Item item){
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailabilityStatus(),
                item.getRequestId()
        );
    }
}
