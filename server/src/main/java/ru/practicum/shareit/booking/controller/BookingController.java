package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    BookingDto addBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) int userId,
                          @RequestBody BookingSaveDto bookingSaveDto) {
        return bookingService.addBooking(userId, bookingSaveDto);
    }

    @PatchMapping("/{bookingId}")
    BookingDto manageBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) int userId,
                             @PathVariable int bookingId, @RequestParam boolean approved) {
        return bookingService.manageBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingDto getBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) int userId,
                          @PathVariable int bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    Collection<BookingDto> getAllUserBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) int userId,
                                              @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    Collection<BookingDto> getAllUserItemsBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) int userId,
                                                   @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllUserItemsBookings(userId, state);
    }
}
