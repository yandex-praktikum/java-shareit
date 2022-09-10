package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestCreatedDto requestDto, long requestorId);

    List<ItemRequestDto> getAllByRequestorId(long requestorId);

    List<ItemRequestDto> getAll(long userId, int from, int size);

    ItemRequestDto get(long requestId, long userId);
}
