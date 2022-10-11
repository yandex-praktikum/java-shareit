package ru.practicum.shareit.item.requests.dto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.item.requests.model.ItemRequest;
import ru.practicum.shareit.item.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ItemRequestMapper {

    public Collection<ItemRequestDto> getAllOwn(@RequestHeader("X-Sharer-User-Id") Integer requestorId,
                                                ItemRequestService itemRequestService,ItemRequestMapper itemRequestMapper) {
        Collection<ItemRequest> itemRequests = itemRequestService.getAllOwn(requestorId);
        Collection<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtos.add(itemRequestMapper.toDto(itemRequest));
        }
        return itemRequestDtos;
    }

    public Collection<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer requestorId,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             ItemRequestService itemRequestService, ItemRequestMapper itemRequestMapper) {
        Collection<ItemRequest> itemRequests = itemRequestService.getAll(requestorId, from, size);
        Collection<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtos.add(itemRequestMapper.toDto(itemRequest));
        }
        return itemRequestDtos;
    }

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestor(itemRequest.getRequestor());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(itemRequest.getItems());
        return itemRequestDto;
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto, Integer requestorId) {
        ItemRequest itemRequest = new ItemRequest();
        User user = new User();
        user.setId(requestorId);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(user);
        return itemRequest;
    }
}
