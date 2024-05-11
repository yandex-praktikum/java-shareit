package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseBookingDto addBooking(@RequestBody @Valid BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        log.info("Получен запрос POST /bookings");
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int bookingId, @RequestParam (name = "approved") boolean isApproved) {
        log.info("Получен запрос PATCH /bookings/{bookingId}");
        return bookingService.patchBooking(ownerId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") int requesterId, @PathVariable int bookingId) {
        log.info("Получен запрос GET /bookings/{bookingId}");
        return bookingService.getBookingById(requesterId, bookingId);
    }

    @GetMapping
    public List<ResponseBookingDto> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam (value = "state", defaultValue = "ALL", required = false) State state,
                                                        @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                                        @RequestParam (value = "size", defaultValue = "10", required = false) Integer size) {
        log.info("Получен запрос GET /bookings?state={state}");
        return bookingService.getAllUsersBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getAllItemOwnerBookings(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                                            @RequestParam (value = "state", defaultValue = "ALL", required = false) State state,
                                                            @RequestParam (value = "from", defaultValue = "0", required = false) Integer from,
                                                            @RequestParam (value = "size", defaultValue = "10", required = false) Integer size) {
        log.info("Получен запрос GET /bookings/owner?state={state}");
        return bookingService.getAllItemOwnerBookings(ownerId, state, from, size);
    }
}
