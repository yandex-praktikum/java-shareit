package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.utilits.Variables;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    @Autowired
    private BookingService bookingService;

    private final String pathId = "/{bookingId}";

    @PostMapping
    public BookingDto booking(@RequestHeader(value = Variables.USER_ID) Integer bookerId,
                              @Valid @RequestBody @NotNull BookingDto bookingDto) {
        return bookingService.booking(bookerId, bookingDto);
    }

    @PatchMapping(path = pathId)
    public BookingDto approve(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                              @PathVariable Integer bookingId,
                              @RequestParam boolean approved) {
        return bookingService.aprove(ownerId, bookingId, approved);
    }

    @GetMapping(pathId)
    public BookingDto getBooking(@RequestHeader(value = Variables.USER_ID) Integer bookerId,
                                 @PathVariable Integer bookingId) {
        return bookingService.getBooking(bookerId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBooking(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                       @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.getBooking(state, ownerId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookedItemList(@RequestHeader(value = Variables.USER_ID) Integer ownerId,
                                                   @RequestParam(name = "state", required = false, defaultValue = "ALL") String state) {
        return bookingService.ownerItemsBooking(state, ownerId);
    }


}
