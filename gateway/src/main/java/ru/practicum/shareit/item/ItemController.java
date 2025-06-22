package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @RequestBody @Valid ItemDto item) {
        return client.createItem(userId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable(name = "itemId") @Positive Long itemId) {
        return client.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                              @PathVariable(name = "itemId") @Positive Long itemId,
                              @RequestBody ItemDto item) {
        return client.updateItem(userId, itemId, item);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return client.getUserItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                     @RequestParam("text") @NotBlank String text) {
        return client.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createdComment(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                     @PathVariable(name = "itemId") @Positive Long itemId,
                                     @RequestBody @Valid RequestCommentDto comment) {
        return client.createdComment(comment, userId, itemId);
    }
}
