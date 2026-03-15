package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> fetchAllUserItems(@RequestHeader(USER_ID_HEADER) Long ownerId) {
        log.info("GET /items (владелец id={})", ownerId);
        return itemService.getByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> findItems(@RequestParam(name = "text") String query) {
        log.info("GET /items/search?text={}", query);
        return itemService.search(query);
    }

    @PatchMapping("/{itemId}")
    public ItemDto modifyItem(@RequestHeader(USER_ID_HEADER) Long requesterId,
                              @PathVariable(name = "itemId") Long id,
                              @RequestBody ItemDto payload) {
        log.info("PATCH /items/{} от пользователя id={}", id, requesterId);
        return itemService.update(requesterId, id, payload);
    }

    @PostMapping
    public ItemDto addNewItem(@RequestHeader(USER_ID_HEADER) Long creatorId,
                              @Valid @RequestBody ItemDto payload) {
        log.info("POST /items от пользователя id={}", creatorId);
        return itemService.create(creatorId, payload);
    }

    @GetMapping("/{itemId}")
    public ItemDto fetchItemById(@PathVariable(name = "itemId") Long id) {
        log.info("GET /items/{}", id);
        return itemService.getById(id);
    }
}