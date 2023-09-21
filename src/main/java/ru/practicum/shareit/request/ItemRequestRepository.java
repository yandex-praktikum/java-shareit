package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository {
    ItemRequest add(Long userId, ItemRequest request);

    void containsById(Long requestId);

    void containsSameOwner(Long userId, Long requestId);

    ItemRequestDto update(Long userId, ItemRequestDto requestDto, Long requestId);

    List<ItemRequest> getAllForUser(Long userId);

    ItemRequestDto getOneWithOutOwner(Long requestId);

    List<ItemRequestDto> search(String text);

    void delete(Long userId, Long requestId);
}
