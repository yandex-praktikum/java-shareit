package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.api.ValidateCreateRequest;
import ru.practicum.shareit.api.ValidateUpdateRequest;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;

import java.util.List;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                          @RequestBody @Validated(ValidateCreateRequest.class) ItemSaveDto itemSaveDto) {
        return itemClient.addItem(userId, itemSaveDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                             @PathVariable Integer itemId,
                                             @RequestBody @Validated(ValidateCreateRequest.class)
                                             CommentSaveDto commentSaveDto) {
        return itemClient.addComment(userId, itemId, commentSaveDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                             @PathVariable Integer itemId,
                                             @RequestBody @Validated(ValidateUpdateRequest.class)
                                             ItemSaveDto itemSaveDto) {
        return itemClient.updateItem(userId, itemId, itemSaveDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Integer itemId) {
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnerItems(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId) {
        return itemClient.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return text.isBlank() ? ResponseEntity.ok(List.of()) : itemClient.searchItems(text);
    }
}
