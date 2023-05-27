package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.Collections;

@Controller
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@PathVariable Integer id,
                                          @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.getItem(id, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                             @RequestBody @Valid ItemRequestDto requestDto) {
        return itemClient.createItem(userId, requestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemRequestDto requestDto,
                                             @PathVariable Integer id,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemClient.updateItem(requestDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable Integer id) {
        return itemClient.deleteItem(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam String text,
                                             @RequestHeader("X-Sharer-User-Id") Integer userId) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.searchItem(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Integer itemId, @RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @Valid @RequestBody CommentRequestDto requestDto) {
        return itemClient.createComment(itemId, userId, requestDto);
    }
}
