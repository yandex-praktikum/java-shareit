package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestService {
    ItemRequestDto createRequest(int userId, ItemRequest itemRequest);

    List<ItemRequestDto> getAllUserRequests(int userId);

    List<ItemRequestDto> getAllRequests(int userId, int from, int size);

    ItemRequestDto findRequestById(int userId, int requestId);
}
