package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/requests")
@Validated
public class ItemRequestController {
    private final ru.practicum.shareit.request.ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                    @Valid @RequestBody ru.practicum.shareit.request.dto.ItemRequestRequestDto requestDto) {
        return itemRequestClient.createItemRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUser(@RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestClient.getItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemRequestClient.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable Integer requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return itemRequestClient.getItemRequest(requestId, userId);
    }
}
