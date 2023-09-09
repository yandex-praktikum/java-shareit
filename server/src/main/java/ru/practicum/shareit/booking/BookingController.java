package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingDto bookingDto,
                             @RequestHeader("X-Sharer-User-Id") @NotNull Long id) {
        log.info("Начало создания брони {} клиентом id={}",
                bookingDto,
                id);
        return bookingService.create(bookingDto, id);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@NotNull @PathVariable long bookingId,
                             @RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                             @RequestParam @NotNull Boolean approved) {

        log.info("Изменение статуса брони{} владельцем id={}",
                bookingId,
                id);

        return bookingService.update(bookingId, approved, id);
    }

    @GetMapping("/{bookingId}")
    public BookingDto get(@NotNull @PathVariable long bookingId,
                          @RequestHeader("X-Sharer-User-Id") @NotNull Long id) {

        log.info("Запрос брони id={} пользователем id = {}",
                bookingId,
                id);

        return bookingService.get(bookingId, id);
    }

    @GetMapping
    public List<BookingDto> getAllForUser(@RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                          @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                          @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Запрос всех броней для клиента id ={} со статусом = {}}",
                id,
                state);

        return bookingService.getAllForUser(id, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllForOwner(@RequestHeader("X-Sharer-User-Id") @NotNull Long id,
                                           @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Запрос всех броней для клиента id ={} со статусом = {}}",
                id,
                state);

        return bookingService.getAllForOwner(id, state, from, size);
    }
}
