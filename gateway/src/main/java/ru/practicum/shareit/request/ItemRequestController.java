package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createNewRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @Valid @RequestBody RequestItemRequestDto requestItemRequestDto) {
        log.info("Creating request {} with userId={}", requestItemRequestDto, userId);
        return requestClient.postCreateItemRequest(userId, requestItemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all requests with userId={}", userId);

        return requestClient.getAllRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get all requests");

        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId) {
        log.info("Get request by id with requestId={}", requestId);

        return requestClient.getItemRequestById(requestId);
    }
}
