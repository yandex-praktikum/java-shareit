package ru.practicum.shareit.request;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestFullDto;
import ru.practicum.shareit.request.dto.ItemRequestWithTimeDto;

import java.util.List;

@Transactional(readOnly = true)
public interface RequestService {

    @Transactional
    ItemRequestWithTimeDto create(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestFullDto> findAllMyRequests(Long ownerId);

    List<ItemRequestFullDto> findAllRequestsByOtherUsers(Long ownerId, Long from, Long size);

    ItemRequestFullDto findById(Long userId, Long requestId);
}
