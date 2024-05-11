package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Create item {}, userId = {}", itemDto, ownerId);
        return itemClient.addItem(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patchItem(@RequestBody ItemDto itemDto, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Update item {}, userId = {}", itemId, ownerId);
        return itemClient.updateItem(ownerId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int itemId) {
        log.info("Get item {}, userId = {}", itemId, ownerId);
        return itemClient.getItem(ownerId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersItems(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                @PositiveOrZero @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                                @Positive @RequestParam (value = "size", defaultValue = "10",required = false) Integer size) {
        log.info("Get items with ownerId = {}, from = {}, size = {}", ownerId, from, size);
        return itemClient.getItemsPagination(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearch(@RequestParam(value = "text") String text,
                                            @RequestHeader("X-Sharer-User-Id") int ownerId,
                                            @PositiveOrZero @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                            @Positive @RequestParam (value = "size", defaultValue = "10",required = false) Integer size) {
        log.info("Search items with text = {}, from = {}, size = {}", text, from, size);
        return itemClient.search(ownerId, from, size, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestBody @Valid CommentDto commentDto, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int authorId) {
        log.info("Create comment {}, userId = {}, for item {}", commentDto, authorId, itemId);
        return itemClient.addComment(authorId, itemId, commentDto);
    }
}
