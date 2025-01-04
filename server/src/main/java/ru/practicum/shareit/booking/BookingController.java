package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestCreateBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public Collection<BookingDto> getAllByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(required = false, defaultValue = "all") String state) {
        return bookingService.getAllByUserId(userId, state);
    }

    @PostMapping
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody RequestCreateBookingDto requestCreateBookingDto) {

        return bookingService.create(userId, requestCreateBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllByItemOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(required = false, defaultValue = "all") String state) {
        return bookingService.getAllByItemOwnerId(userId, state);
    }
}
