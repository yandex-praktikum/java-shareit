package ru.practicum.shareit.item.service;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemResponseToRequestDto;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    Item map(ItemSaveDto itemSaveDto);

    ItemDto map(Item item);

    Collection<ItemDto> map(Collection<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(@MappingTarget Item item, ItemSaveDto itemDto);

    Collection<ItemResponseToRequestDto> mapToResponseToRequest(Collection<Item> items);
}
