package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UserIdMissingException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createNewRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody RequestItemRequestDto requestItemRequestDto) {
        validateUserId(userId);
        return service.create(userId, requestItemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        validateUserId(userId);
        return service.findAllRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        validateUserId(userId);
        return service.findAll(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable Long requestId) {
        return service.findItemRequestBy(requestId);
    }

    private void validateUserId(Long userId) {
        if (userId == null)
            throw new UserIdMissingException("Заголовок \"X-Sharer-User-Id\" отсутствует");
    }
}
