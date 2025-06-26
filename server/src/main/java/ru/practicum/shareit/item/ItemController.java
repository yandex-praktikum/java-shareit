package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    /**
     * Создание новой вещи.
     * Post /items
     * Headers X-Sharer-User-Id
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto item) {
        return itemService.createItem(userId, item);
    }

    /**
     * Возвращает вещь по её id.
     * GET /items/{itemId}
     */
    @GetMapping("/{itemId}")
    public ItemWithCommentDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    /**
     * Обновление данных о вещи.
     * GET /items/{itemId}
     * Headers X-Sharer-User-Id
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long itemId,
                              @RequestBody ItemDto item) {
        return itemService.updateItem(userId, itemId, item);
    }

    /**
     * Получает список вещей пользователя.
     * GET /items
     * Headers X-Sharer-User-Id
     */
    @GetMapping
    public List<ItemWithCommentDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getUserItems(userId);
    }

    /**
     * Получает список вещей, которые подходят по поиску.
     * GET /items/search?text={text}
     */
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam("text") String text) {
        return itemService.searchItems(userId, text);
    }

    /**
     * Создание комментария
     * POST /items/itemId
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto createdComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long itemId,
                                     @RequestBody CommentDto comment) {
        return commentService.createdComment(userId, itemId, comment);
    }
}
