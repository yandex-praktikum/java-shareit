package ru.practicum.shareit.request.service;

import ru.practicum.shareit.base.service.BaseService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService extends BaseService<ItemRequest, Long> {
    ItemRequestDto create(Long userId, RequestItemRequestDto itemRequestDto);

    Collection<ItemRequestDto> findAllRequestsOfUser(Long userId);

    Collection<ItemRequestDto> findAll(Long userId);

    ItemRequestDto findItemRequestBy(Long id);
}
