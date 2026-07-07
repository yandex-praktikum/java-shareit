package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;  // ← не ItemService, а ItemClient!

    @GetMapping
    public ResponseEntity<Object> findAllItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GET /items - все предметы пользователя {}", userId);
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @PathVariable long itemId) {
        log.info("GET /items/{} - предмет от пользователя {}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> createNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @Validated(ItemRequestDto.OnCreate.class)
                                                @RequestBody ItemRequestDto dto) {
        log.info("POST /items - создание от пользователя {}", userId);
        return itemClient.createItem(userId, dto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @Validated(ItemRequestDto.OnUpdate.class)
                                             @RequestBody ItemRequestDto dto) {
        log.info("PATCH /items/{} - обновление от пользователя {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, dto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId) {
        log.info("DELETE /items/{} - удаление от пользователя {}", itemId, userId);
        return itemClient.deleteItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text) {
        log.info("GET /items/search?text={} - поиск от пользователя {}", text, userId);
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("POST /items/{}/comment - комментарий от пользователя {}", itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}