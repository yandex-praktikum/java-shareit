package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long requestorId);

    ItemRequestDto updateItemRequest(Long requestId, ItemRequestDto itemRequestDto);

    ItemRequestDto getItemRequestById(Long requestId);
}
