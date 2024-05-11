package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@Slf4j
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") int requesterId) {
        log.info("Получен запрос POST /requests");
        return itemRequestService.addItemRequest(itemRequestDto, requesterId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequesterItemRequests(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        log.info("Получен запрос GET /requests");
        return itemRequestService.getAllRequesterItemRequests(requesterId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") int requesterId, @PathVariable int requestId) {
        log.info("Получен запрос GET /requests");
        return itemRequestService.getItemRequestById(requesterId, requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequesterItemRequestsPagination(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                                                      @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                                                      @RequestParam (value = "size", defaultValue = "10", required = false) Integer size) {
        log.info("Получен запрос GET /requests/all");
        return itemRequestService.getAllRequesterItemRequestsPagination(requesterId, from, size);
    }
}
