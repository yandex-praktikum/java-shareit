package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingDto bookingDto,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long id) {
        log.info("Начало создания брони {} клиентом id={}",
                bookingDto,
                id);
        return bookingClient.create(id, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@NotNull @PathVariable long bookingId,
                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                         @RequestParam @NotNull Boolean approved) {

        log.info("Изменение статуса брони{} владельцем id={}",
                bookingId,
                id);

        return bookingClient.update(bookingId, id, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> get(@NotNull @PathVariable long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") @NotNull Long id) {

        log.info("Запрос брони id={} пользователем id = {}",
                bookingId,
                id);

        return bookingClient.get(bookingId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getAllForUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                                @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Запрос всех броней для клиента id ={} со статусом = {}}",
                id,
                state);

        return bookingClient.getAllForUser(id, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllForOwner(@RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                                 @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                 @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Запрос всех броней для клиента id ={} со статусом = {}}",
                id,
                state);

        return bookingClient.getAllForOwner(id, state, from, size);
    }
}
