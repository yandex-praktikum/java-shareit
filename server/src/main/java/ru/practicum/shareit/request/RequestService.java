package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto create(RequestDto requestDto, Long userId);

    List<RequestDto> getAllUserRequest(Long userId);

    RequestDto get(Long requestId, Long userId);

    List<RequestDto> getAllNoUserRequest(Long userId, Integer from, Integer size);
}
