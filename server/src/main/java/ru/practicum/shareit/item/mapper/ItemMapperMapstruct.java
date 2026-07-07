package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.model.ItemRequestDto;
import ru.practicum.shareit.item.model.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface ItemMapperMapstruct {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "request", ignore = true)
    Item mapToItem(ItemRequestDto dto, User owner);

    @Mapping(target = "ownerId", source = "owner.id")
    ItemResponseDto mapToDto(Item item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    void updateItemFromDto(ItemRequestDto dto, @MappingTarget Item item);
}