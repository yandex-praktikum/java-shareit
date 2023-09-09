package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
    final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody RequestDto requestDto,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Начало сохранение запроса {} пользователя id={}", requestDto, userId);
        return requestClient.create(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        log.info("Начало поиска запросов пользователя id={}", userId);
        return requestClient.getAllUserRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllNoUserRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Начало поиска запросов других пользователей");
        return requestClient.getAllNoUserRequest(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getUserRequest(@RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                 @PathVariable @NotNull Long requestId) {
        log.info("Начало поиска запроса {}", requestId);
        return requestClient.get(requestId, userId);
    }

}
