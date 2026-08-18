package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public class ItemRequestMapper {

    public static ItemRequest toEntity(ItemRequestCreateDto dto) {
        ItemRequest request = new ItemRequest();
        request.setDescription(dto.getDescription());
        return request;
    }

    public static ItemRequestDto toDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();

        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());

        if (request.getItems() != null) {
            dto.setItems(request.getItems().stream()
                    .map(ItemRequestMapper::toAnswerDto)
                    .toList());
        } else {
            dto.setItems(List.of());
        }

        return dto;
    }

    private static ItemRequestAnswerDto toAnswerDto(Item item) {
        ItemRequestAnswerDto dto = new ItemRequestAnswerDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwner().getId());

        return dto;
    }

    public static List<ItemRequestDto> toDtoList(List<ItemRequest> requests) {
        return requests.stream()
                .map(ItemRequestMapper::toDto)
                .toList();
    }
}