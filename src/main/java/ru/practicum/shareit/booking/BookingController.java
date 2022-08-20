package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingStatusDto create(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                             @Valid @RequestBody BookingDto bookingDto) {
        return bookingService.create(bookerId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public BookingStatusDto approve(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                    @PathVariable Long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.approve(ownerId, bookingId, approved);
    }

    @GetMapping("{bookingId}")
    public BookingStatusDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    public List<BookingStatusDto> getByBookerId(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                    @RequestParam(required = false,
                                                            defaultValue = "ALL") String state) {
        return bookingService.getByBookerId(bookerId, state);
    }

    @GetMapping("owner")
    public List<BookingStatusDto> getByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                   @RequestParam(required = false,
                                                           defaultValue = "ALL") String state) {
        return bookingService.getByOwnerId(ownerId, state);
    }

}
