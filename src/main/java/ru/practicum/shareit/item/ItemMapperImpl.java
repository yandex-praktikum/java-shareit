package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
@Mapper
public class ItemMapperImpl implements ItemMapper {

    public ItemDto itemToItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable());
    }

    public Item itemDtoToItem(ItemDto itemDto) {
        return new Item(null, null, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable());
    }

}
