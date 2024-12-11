package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestSaveDto;

@Controller
@RequestMapping("/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                                    @RequestBody @Valid ItemRequestSaveDto itemRequestSaveDto) {
        return itemRequestClient.createItemRequest(userId, itemRequestSaveDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId) {
        return itemRequestClient.getAllUserItemRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests() {
        return itemRequestClient.getAllItemRequests();
    }


    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable @PositiveOrZero Integer requestId) {
        return itemRequestClient.getItemRequest(requestId);
    }
}
