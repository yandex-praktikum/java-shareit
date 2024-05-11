package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requester) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                requester,
                LocalDateTime.now()
        );
    }

    public static List<ItemRequestDto> listToItemRequestDto(Iterable<ItemRequest> itemRequests) {
        List<ItemRequestDto> dtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            dtos.add(toItemRequestDto(itemRequest));
        }
        return dtos;
    }
}
