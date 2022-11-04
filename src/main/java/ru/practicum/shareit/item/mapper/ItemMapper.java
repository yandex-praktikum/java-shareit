package ru.practicum.shareit.item.mapper;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
@Getter
public class ItemMapper {
    private Long id = 0L;

    private Long generateId() {
        ++id;
        return getId();
    }

    public Item fromItemDtoCreate(ItemDto itemDto) {
        return new Item(generateId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    public Item fromItemDtoUpdate(ItemDto itemDto) {
        return new Item(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

    public ItemDto fromItem(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

}
