package ru.practicum.shareit.request;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequest add(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemRequest request) {
        return itemRequestService.add(userId, request);
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody ItemRequestDto requestDto, @PathVariable Long requestId) {
        return itemRequestService.update(userId, requestDto, requestId);
    }

    @GetMapping
    public List<ItemRequest> getAllForUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllForUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getOne(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        return itemRequestService.getOne(userId, requestId);
    }

    @GetMapping("/search")
    public List<ItemRequestDto> search(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam String text) {
        return itemRequestService.search(userId, text);
    }

    @DeleteMapping("/{requestId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        itemRequestService.delete(userId, requestId);
    }
}
