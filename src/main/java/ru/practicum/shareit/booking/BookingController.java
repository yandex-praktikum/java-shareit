package ru.practicum.shareit.booking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StatusDto;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto add(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updApprove(@PathVariable Integer bookingId, @RequestParam @NonNull Boolean approved,
                                 @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.updApprove(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@PathVariable Integer bookingId, @RequestHeader("X-Sharer-User-Id") Integer userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> findAllByUser(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                @RequestParam(defaultValue = "ALL") StatusDto state) {
        return bookingService.findAllByUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByOwner(@RequestHeader("X-Sharer-User-Id") Integer userId,
                                                 @RequestParam(defaultValue = "ALL") StatusDto state) {
        return bookingService.findAllByOwner(userId, state);
    }

}
