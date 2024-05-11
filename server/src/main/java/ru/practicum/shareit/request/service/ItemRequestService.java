package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(ItemRequestDto itemRequestDto, int requesterId);

    List<ItemRequestDto> getAllRequesterItemRequests(int requesterId);

    ItemRequestDto getItemRequestById(int requesterId, int requestId);

    List<ItemRequestDto> getAllRequesterItemRequestsPagination(int requesterId, Integer from, Integer size);
}
