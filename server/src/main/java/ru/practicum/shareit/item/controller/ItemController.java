package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос POST /items");
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestBody ItemDto itemDto, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос PATCH /items/{itemId}");
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoBookingsAndComments getItemById(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int itemId) {
        log.info("Получен запрос GET /items/{itemId}");
        return itemService.getItemById(ownerId, itemId);
    }

    @GetMapping
    public List<ItemDtoBookingsAndComments> getUserItems(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                         @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                                         @RequestParam (value = "size", defaultValue = "10",required = false) Integer size) {
        log.info("Получен запрос GET /items");
        return itemService.getUserItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearch(@RequestParam(value = "text") String text,
                                   @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                   @RequestParam (value = "size", defaultValue = "10",required = false) Integer size) {
        log.info("Получен запрос GET /search");
        return itemService.getSearch(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody @Valid CommentDto commentDto, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int authorId) {
        log.info("Получен запрос POST /items");
        return itemService.addComment(commentDto, itemId, authorId);
    }
}
