package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemService.create(itemDto, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestBody ItemUpdateDto itemDto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.update(itemDto, userId, itemId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDtoWithBookingDates> getItemById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                               @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.findItemById(itemId, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemDtoWithBookingDates>> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                           @RequestParam(required = false) Long from,
                                                                           @RequestParam(required = false) Long size) {
        return ResponseEntity.ok(itemService.getAllByUserId(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getItemByText(@RequestParam(required = false) String text,
                                       @RequestParam(required = false) Long from,
                                       @RequestParam(required = false) Long size) {
        return ResponseEntity.ok(itemService.search(text, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentFullDto> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long itemId,
                                        @RequestBody CommentDto comment) {
        if (comment.getText().isBlank()) {
            throw new ValidationException("text is empty");
        }
        return ResponseEntity.ok(itemService.createComment(comment, itemId, userId));
    }
}
