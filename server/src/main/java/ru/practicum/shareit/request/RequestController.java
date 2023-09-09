package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestController {
    final RequestService requestService;

    @PostMapping
    public RequestDto create(@Valid @RequestBody RequestDto requestDto,
                             @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Начало сохранение запроса {} пользователя id={}", requestDto, userId);
        return requestService.create(requestDto, userId);
    }

    @GetMapping
    public List<RequestDto> getAllUserRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Начало поиска запросов пользователя id={}", userId);
        return requestService.getAllUserRequest(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllNoUserRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Начало поиска запросов других пользователей");
        return requestService.getAllNoUserRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestDto getUserRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                        @PathVariable @NotNull Long requestId) {
        log.info("Начало поиска запроса {}", requestId);
        return requestService.get(requestId, userId);
    }

}
