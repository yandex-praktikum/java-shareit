package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @PostMapping
    public ItemRequestDto add(@RequestBody ItemRequestDto itemRequestDto,
                              @RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, requestorId);
        return itemRequestMapper.toDto(itemRequestService.add(itemRequest));
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllOwn(@RequestHeader("X-Sharer-User-Id") Integer requestorId) {
        Collection<ItemRequest> itemRequests = itemRequestService.getAllOwn(requestorId);
        Collection<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtos.add(itemRequestMapper.toDto(itemRequest));
        }
        return itemRequestDtos;
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
        Collection<ItemRequest> itemRequests = itemRequestService.getAll(requestorId, from, size);
        Collection<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestDtos.add(itemRequestMapper.toDto(itemRequest));
        }
        return itemRequestDtos;
    }
}
