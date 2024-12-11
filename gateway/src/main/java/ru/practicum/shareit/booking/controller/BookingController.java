package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exceptions.NotValidException;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                             @RequestBody @Valid BookingSaveDto bookingSaveDto) {
        return bookingClient.addBooking(userId, bookingSaveDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> manageBooking(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                                @PathVariable Integer bookingId,
                                                @RequestParam Boolean approved) {
        return bookingClient.manageBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                             @PathVariable Integer bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBookings(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                                     @RequestParam(defaultValue = "all") String state,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        BookingState bookingState = getBookingState(state);
        return bookingClient.getAllUserBookings(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllUserItemsBookings(@RequestHeader(RequestHttpHeaders.USER_ID) Integer userId,
                                                          @RequestParam(defaultValue = "all") String state) {
        BookingState bookingState = getBookingState(state);

        return bookingClient.getAllUserItemsBookings(userId, bookingState);
    }

    private BookingState getBookingState(String state) {
        return BookingState.from(state)
                .orElseThrow(() -> new NotValidException(BookingState.class, state + " not valid"));
    }

}
