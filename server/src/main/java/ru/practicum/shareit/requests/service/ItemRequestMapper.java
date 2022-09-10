package ru.practicum.shareit.requests.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.requests.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestMapper {
    ItemRepository itemRepository;

    public ItemRequestDto toItemRequestDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestor(dto.getRequestor());
        dto.setCreated(request.getCreated());

        dto.setItems(itemRepository.findAllByRequestId(request.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList()));

        return dto;
    }

    public ItemRequest toItemRequestFromCreateDto(ItemRequestCreatedDto dto) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        request.setCreated(LocalDateTime.now());
        return request;
    }
}
