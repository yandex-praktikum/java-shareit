package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.dto.RequestItemDto;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getAllItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable Long itemId) {
        log.info("Get Item{id={}}", itemId);

        return itemClient.getItemById(itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByNameAndDescription(@RequestParam("text") String text) {
        log.info("Get Items of search by name/description with text = {}", text);

        return itemClient.getBySearchByNameAndDescription(text);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Valid @RequestBody RequestItemDto itemDto) {
        log.info("Creating item {} with userId={}", itemDto, userId);

        return itemClient.postCreateItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody RequestItemDto itemDto) {
        log.info("Patch Item{id={}} {} with userId={}", itemId, itemDto, userId);

        return itemClient.patchItem(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @PathVariable Long itemId,
                                                   @Valid @RequestBody RequestCommentDto requestCommentDto) {
        log.info("Add comment {} to Item{id={}} with userId={}", requestCommentDto, itemId, userId);

        return itemClient.postSaveCommentToItem(userId, itemId, requestCommentDto);
    }
}
