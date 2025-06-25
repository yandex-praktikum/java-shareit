package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public class RequestMapper {
    public static ItemRequest mapToRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        //  itemRequest.setId(itemRequestDto.getId() != null ? itemRequestDto.getId() : null);  //оставил на всякий случай
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(itemRequestDto.getRequestor());
        //   itemRequest.setCreated(itemRequestDto.getCreated());        //оставил на всякий случай
        return itemRequest;
    }

    public static ItemRequestDto mapToRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        //   itemRequestDto.setId(itemRequest.getId());      //оставил на всякий случай
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestor(itemRequest.getRequestor());
        //  itemRequestDto.setCreated(itemRequest.getCreated());         //оставил на всякий случай

        return itemRequestDto;
    }

}
