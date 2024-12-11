package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(int userId, ItemRequestSaveDto itemRequestSaveDto);

    Collection<ItemRequestDto> getAllUserItemRequest(int userId);

    Collection<ItemRequestDto> getAllItemRequests();

    ItemRequestDto getItemRequest(int requestId);
}
