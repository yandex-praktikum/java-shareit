package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Creating item request by user with id {}", userId);
        return requestClient.addRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findRequestsByRequestor(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Find all requests created by user with id {}", requestorId);
        return requestClient.findRequestByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findRequestsWithPagination(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @PositiveOrZero @RequestParam(required = false) Integer from,
                                                             @Positive @RequestParam(required = false) Integer size) {
        log.info("Find all requests with pagination created by user with id {}", userId);
        return requestClient.findAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable Long requestId,
                                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get request with id {} by user with id {}",
                requestId, userId);
        return requestClient.getRequestById(requestId, userId);
    }
}
