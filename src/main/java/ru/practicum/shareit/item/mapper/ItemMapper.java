package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/**
 * Маппер между объектами Item и ItemDto
 */
@Mapper
public interface ItemMapper {
    Item toItem(ItemDto dto);

    ItemDto toDto(Item item);
}
