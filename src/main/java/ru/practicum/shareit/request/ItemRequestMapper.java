package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {
    public static ItemRequestDto inItemReqestDto(ItemRequest itemRequest){
        return new ItemRequestDto(
                itemRequest.getRequestor(),
                itemRequest.getDescription(),
                itemRequest.getCreated()
        );
    }
}
