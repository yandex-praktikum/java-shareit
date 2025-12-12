package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId(),
                itemRequest.getCreated()
        );
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<Item> items) {
        List<ItemRequestDto.ItemInfo> itemInfos = Collections.emptyList(); // вместо null
        if (items != null && !items.isEmpty()) {
            itemInfos = items.stream()
                    .map(item -> {
                        ItemRequestDto.ItemInfo itemInfo = new ItemRequestDto.ItemInfo();
                        itemInfo.setId(item.getId());
                        itemInfo.setName(item.getName());
                        itemInfo.setOwnerId(item.getOwnerId());
                        return itemInfo;
                    })
                    .collect(Collectors.toList());
        }

        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getRequestorId(),
                itemRequest.getCreated(),
                itemInfos
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(
                itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                itemRequestDto.getRequestorId(),
                itemRequestDto.getCreated()
        );
    }
}
