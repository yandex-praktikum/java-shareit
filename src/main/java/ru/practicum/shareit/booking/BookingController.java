package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utilits.Variables;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    private final String pathId = "/{bookingId}";

    @PostMapping
    public BookingDto booking(@RequestHeader(value = Variables.USER_ID) Integer bookerId,
                              @Valid @RequestBody @NotNull BookingDto bookingDto) {
        return bookingService.booking(bookerId, bookingDto);
    }

    @PatchMapping(pathId)
    public BookingDto approve(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                              @PathVariable Integer bookingId,
                              @RequestParam(name = "approved") boolean approved) {
        return bookingService.aprove(ownerId, bookingId, approved);
    }

    @GetMapping(pathId)
    public BookingDto getBooking(@RequestHeader(value = Variables.USER_ID) Integer bookerId,
                                 @PathVariable Integer bookingId) {
        return bookingService.getBooking(bookerId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBooking(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                       @RequestParam(name = "state",
                                               required = false,
                                               defaultValue = "ALL") BookingStatus state,
                                       @RequestParam(required = false, name = "from") Integer from,
                                       @RequestParam(required = false, name = "size") Integer size) {
        return bookingService.getBooking(state, ownerId, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookedItemList(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                                   @RequestParam(name = "state", required = false, defaultValue = "ALL") BookingStatus state,
                                                   @RequestParam(required = false, name = "from") Integer from,
                                                   @RequestParam(required = false, name = "size") Integer size) {
        return bookingService.ownerItemsBookingLists(state, ownerId, from, size);
    }
}
