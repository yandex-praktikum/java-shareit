package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addItemRequest(Integer userId, ItemRequestDto itemRequestDto);

    ItemRequestDto getItemRequest(Integer userId, Integer id);

    List<ItemRequestDto> getItemRequests(Integer owner,Integer from, Integer size);

    List<ItemRequestDto> getItemRequests(Integer userId);

}
