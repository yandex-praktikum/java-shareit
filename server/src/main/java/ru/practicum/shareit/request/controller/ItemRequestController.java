package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                            @RequestBody ItemRequestSaveDto itemRequestSaveDto) {
        return itemRequestService.createItemRequest(userId, itemRequestSaveDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllUserItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId) {
        return itemRequestService.getAllUserItemRequest(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequests() {
        return itemRequestService.getAllItemRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@PathVariable int requestId) {
        return itemRequestService.getItemRequest(requestId);
    }
}
