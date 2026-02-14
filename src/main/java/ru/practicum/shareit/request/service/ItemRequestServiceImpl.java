package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.storage.InMemoryItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final InMemoryItemRequestStorage requestStorage;
    private final InMemoryUserStorage userStorage;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long requestorId) {
        User requestor = userStorage.findById(requestorId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + requestorId + " не найден"));

        ItemRequest itemRequest = ItemRequestMapper.fromItemRequestDto(itemRequestDto, requestor);
        ItemRequest createdRequest = requestStorage.create(itemRequest);
        return ItemRequestMapper.toItemRequestDto(createdRequest);
    }

    @Override
    public ItemRequestDto updateItemRequest(Long requestId, ItemRequestDto itemRequestDto) {
        ItemRequest existingRequest = requestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос вещи с id " + requestId + " не найден"));

        if (itemRequestDto.getDescription() != null) {
            existingRequest.setDescription(itemRequestDto.getDescription());
        }
        if (itemRequestDto.getCreated() != null) {
            existingRequest.setCreated(itemRequestDto.getCreated());
        }

        ItemRequest updatedRequest = requestStorage.update(existingRequest);
        return ItemRequestMapper.toItemRequestDto(updatedRequest);
    }

    @Override
    public ItemRequestDto getItemRequestById(Long requestId) {
        ItemRequest itemRequest = requestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос вещи с id " + requestId + " не найден"));
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }
}
