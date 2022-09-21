package ru.practicum.shareit.item.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.request.dto.ItemRequestDto;
import ru.practicum.shareit.item.request.dto.RequestDto;
import ru.practicum.shareit.item.request.model.ItemRequest;

import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public static RequestDto toRequestDto(ItemRequest itemRequest) {
        return RequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toItemRequest(RequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
                .items(items)
                .build();
    }
}