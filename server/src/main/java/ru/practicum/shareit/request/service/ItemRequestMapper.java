package ru.practicum.shareit.request.service;

import org.mapstruct.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequest map(ItemRequestSaveDto itemRequestSaveDto);

    ItemRequestDto map(ItemRequest itemRequest);

    Collection<ItemRequestDto> map(Collection<ItemRequest> itemRequests);
}
