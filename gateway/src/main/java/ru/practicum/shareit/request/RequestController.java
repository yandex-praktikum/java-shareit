package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto, @RequestHeader("X-Sharer-User-Id") int requesterId) {
        log.info("Create request {}", itemRequestDto);
        return requestClient.add(requesterId, itemRequestDto);
    }

    @GetMapping
    private ResponseEntity<Object> getAllRequesterItemRequests(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        log.info("Get requests with userId = {}", requesterId);
        return requestClient.getItemRequests(requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestsById(@RequestHeader("X-Sharer-User-Id") int requesterId, @PathVariable int requestId) {
        log.info("Get request {}, userId = {}", requestId, requesterId);
        return requestClient.getItemRequest(requesterId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequesterItemRequestsPagination(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                                                        @PositiveOrZero @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                                                        @Positive @RequestParam (value = "size", defaultValue = "10", required = false) Integer size) {
        log.info("Get requests with userId = {}, from = {}, size = {}", requesterId, from, size);
        return requestClient.getItemRequestsPagination(requesterId, from, size);
    }
}
