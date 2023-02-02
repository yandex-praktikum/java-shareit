package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.exception.UnknownStatusException;
import ru.practicum.utilities.Constants;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService service;

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@PathVariable Long bookingId,
                                             @RequestHeader(Constants.USER_ID_HEADER) Long userId) {
        return service.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingByOwner(@RequestHeader(Constants.USER_ID_HEADER) Long ownerId,
                                                      @RequestParam(required = false, defaultValue = "ALL") String state,
                                                      @RequestParam(required = false) Integer from,
                                                      @RequestParam(required = false) Integer size)
            throws UnknownStatusException {
        return service.getByOwnerId(ownerId, state, from, size);
    }

    @GetMapping()
    public List<BookingDtoResponse> getAllBookings(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                                   @RequestParam(required = false, defaultValue = "ALL") String state,
                                                   @RequestParam(required = false) Integer from,
                                                   @RequestParam(required = false) Integer size)
            throws UnknownStatusException {
        return service.getByUserId(userId, state, from, size);
    }

    @PostMapping
    public BookingDtoResponse createBooking(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                            @Valid @RequestBody BookingDto bookingDto) {
        return service.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse updateBooking(@RequestHeader(Constants.USER_ID_HEADER) Long userId,
                                    @PathVariable Long bookingId,
                                    @RequestParam(required = false) Boolean approved) {
        return service.update(userId, bookingId, approved);
    }
}
