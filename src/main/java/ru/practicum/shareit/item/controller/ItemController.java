package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> addItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody ItemDto itemDto) {
        return ok(itemService.createItem(userId, itemDto));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable Long itemId,
                                        @RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestBody ItemDto itemDto) {
        return ok(itemService.updateItem(itemId, userId, itemDto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable Long itemId,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    @GetMapping
    public ResponseEntity<?> getListOfItem(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemService.returnListOfItems(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getItemByText(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam String text) {
        return ResponseEntity.ok(itemService.itemByText(userId, text));
    }

}
