package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingFullDto> createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestBody BookingDto booking) {
        return ResponseEntity.ok(bookingService.create(booking, userId));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingFullDto> updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @PathVariable Long bookingId,
                                                        @RequestParam String approved) {
        return ResponseEntity.ok(bookingService.update(approved, bookingId, userId));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingFullDto> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.get(userId, bookingId));
    }

    @GetMapping(params = "state")
    public ResponseEntity<List<BookingFullDto>> getBookingsByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                  @RequestParam(defaultValue = "ALL") String state) {
        return ResponseEntity.ok(bookingService.getByUserIdAndState(userId, BookingState.from(state).orElseThrow(
                () -> new ValidationException("Unknown state: UNSUPPORTED_STATUS")
        )));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingFullDto>> getBookingsByItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                                  @RequestParam(required = false) String state,
                                                                  @RequestParam(required = false) Long from,
                                                                  @RequestParam(required = false) Long size) {
        if (state != null) {
            BookingState.from(state).orElseThrow(
                    () -> new ValidationException("Unknown state: UNSUPPORTED_STATUS")
            );
        } else {
            return ResponseEntity.ok(bookingService
                    .getAllBookingByItemsForUserId(userId, BookingState.ALL, from, size));
        }
        return ResponseEntity.ok(bookingService.getAllBookingByItemsForUserId(
                userId,
                BookingState.from(state)
                        .orElseThrow(() -> new ValidationException("Unknown state: UNSUPPORTED_STATUS")),
                from, size));
    }

    @GetMapping
    public ResponseEntity<List<BookingFullDto>> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                       @RequestParam(required = false) Long from,
                                                       @RequestParam(required = false) Long size) {
        return ResponseEntity.ok(bookingService.getAllByUserId(userId, from, size));
    }
}
