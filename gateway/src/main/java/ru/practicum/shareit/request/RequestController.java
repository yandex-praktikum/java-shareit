package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestAddDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestClient client;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestBody @Valid RequestAddDto request,
                                                @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return client.createRequest(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return client.getRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return client.getRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable(name = "requestId") @Positive Long requestId,
                                         @RequestHeader("X-Sharer-User-Id") @Positive Long userId) {
        return client.getRequestById(requestId, userId);
    }
}
