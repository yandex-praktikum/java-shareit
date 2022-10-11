package ru.practicum.shareit.item.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.requests.dto.ItemRequestMapper;
import ru.practicum.shareit.item.requests.model.ItemRequest;
import ru.practicum.shareit.item.requests.dto.ItemRequestDto;
import ru.practicum.shareit.item.requests.service.ItemRequestService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequestDto add(@Valid @RequestBody ItemRequestDto itemRequestDto,
                              @RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, requestorId);
        return itemRequestMapper.toDto(itemRequestService.add(itemRequest));
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllOwn(@RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        return itemRequestMapper.getAllOwn(requestorId, itemRequestService, itemRequestMapper);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable Integer requestId,
                                  @RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        return itemRequestMapper.toDto(itemRequestService.getById(requestId, requestorId));
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Integer requestorId,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {

        return itemRequestMapper.getAll(requestorId, from, size, itemRequestService, itemRequestMapper);
    }
}