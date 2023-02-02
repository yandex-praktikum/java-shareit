package ru.practicum.request;

import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoResponse addRequest(ItemRequestDto requestDto, Long ownerId);

    List<ItemRequestDtoResponse> getRequestByOwnerId(Long ownerId);

    List<ItemRequestDtoResponse> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestDtoResponse getRequestById(Long requestId, Long userId);

    ItemRequestDtoResponse map(ItemRequest request);
}
