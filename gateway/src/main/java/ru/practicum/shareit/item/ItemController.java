package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;


@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid ItemCreateDto requestDto) {

        log.info("Creating item, userId={}", userId);

        return itemClient.create(userId, requestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody ItemUpdateDto requestDto) {

        log.info("Updating item {}, userId={}", itemId, userId);

        return itemClient.update(userId, itemId, requestDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId) {

        log.info("Getting item {}, userId={}", itemId, userId);

        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        log.info("Getting items, userId={}", userId);

        return itemClient.getItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(
            @RequestParam @NotBlank String text) {

        log.info("Searching items, text={}", text);

        return itemClient.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody @Valid CommentRequestDto requestDto) {

        log.info("Adding comment to item {}, userId={}", itemId, userId);

        return itemClient.addComment(userId, itemId, requestDto);
    }
}