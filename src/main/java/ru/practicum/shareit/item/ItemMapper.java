package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {
    public ItemDto itemDto(Item item) {
        return new ItemDto()
                .setId(item.getId())
                .setName(item.getName())
                .setOwnerId(item.getOwnerId())
                .setRequest(item.getRequest())
                .setDescription(item.getDescription())
                .setAvailable(item.getAvailable());
    }
}
