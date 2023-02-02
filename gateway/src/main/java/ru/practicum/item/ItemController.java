package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Constants;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/items")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
                                              @RequestHeader(Constants.USER_ID_HEADER) Long ownerId) {
        log.info("Get item by id {}, ownerId={}", itemId, ownerId);
        return itemClient.getItemById(itemId, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllItems(@RequestHeader(Constants.USER_ID_HEADER) Long userId) {
        log.info("Get all items by user with id {}", userId);
        return itemClient.findAllItems(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Create item by user with id {}", userId);
        return itemClient.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("Update item with id {} by user with id {}", itemId, userId);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        log.info("Search items by text \"{}\"", text);
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.EMPTY_LIST);
        }
        return itemClient.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                             @PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Add comment to item with id {} by user with id {}", itemId, userId);
        return itemClient.addComment(commentDto, userId, itemId);
    }
}
