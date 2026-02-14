package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

/**
 * TODO Sprint add-item-requests.
 */

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        return itemRequestService.createItemRequest(itemRequestDto, requestorId);
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto updateItemRequest(@PathVariable Long requestId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.updateItemRequest(requestId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable Long requestId) {
        return itemRequestService.getItemRequestById(requestId);
    }
}
