package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.Variables;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    private final String pathId = "/{bookingId}";

    @PostMapping
    public ResponseEntity<Object> booking(@RequestHeader(value = Variables.USER_ID) Integer userId,
                                          @Valid @RequestBody @NotNull BookingDto bookingDto) {
        return bookingClient.addBooking(userId, bookingDto);
    }

    @PatchMapping(pathId)
    public ResponseEntity<Object> approve(@RequestHeader(value = Variables.USER_ID) Integer userId,
                                          @PathVariable(name = "bookingId") Integer bookingId,
                                          @RequestParam(name = "approved") boolean approved) {
        return bookingClient.aprove(userId, bookingId, approved);
    }

    @GetMapping(pathId)
    public ResponseEntity<Object> getBooking(@RequestHeader(value = Variables.USER_ID) Integer userId,
                                             @PathVariable Integer bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBooking(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                             @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
            return bookingClient.getBookings(ownerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookedItemList(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                                         @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingClient.getOwnerItemsBooking(ownerId, state);
    }
}
