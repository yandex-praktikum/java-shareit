package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestDtoMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        if (itemRequest == null) return null;
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getRequestId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated()).build();

        if (itemRequest.getItems() != null) {
            itemRequestDto.setItems(ItemDtoMapper.toItemSmallDtoList(itemRequest.getItems()));
        }

        return itemRequestDto;
    }

    public static ItemRequest toItemRequest(ItemRequestCreateRequest itemRequestCreateRequest) {
        return ItemRequest.builder()
                .description(itemRequestCreateRequest.getDescription())
                .build();
    }

    public static List<ItemRequestDto> toItemRequestDtoList(List<ItemRequest> itemRequestList) {
        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        if (itemRequestList != null) {
            for (ItemRequest itemRequest : itemRequestList) {
                itemRequestDtoList.add(toItemRequestDto(itemRequest));
            }
        }
        return itemRequestDtoList;
    }
}