package ru.practicum.shareit.item.request.service;

import ru.practicum.shareit.item.request.dto.ItemRequestDto;
import ru.practicum.shareit.item.request.dto.RequestDto;

import java.util.List;

public interface ItemRequestService {
    RequestDto create(long userId, RequestDto itemRequestDto);

    List<ItemRequestDto> getByRequestor(long userId);

    List<ItemRequestDto> getAll(long userId, int from, int size);

    ItemRequestDto getById(long userId, long requestId);
}
