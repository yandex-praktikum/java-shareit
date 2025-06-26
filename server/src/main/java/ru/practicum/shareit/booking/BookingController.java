package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    /**
     * Создание брони.
     * Post /bookings
     * Headers X-Sharer-User-Id
     */
    @PostMapping
    public BookingResponseDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(userId, bookingDto);
    }

    /**
     * Изменение состояния брони.
     * Patch /bookings/bookingId?approved
     * Headers X-Sharer-User-Id
     */
    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@PathVariable("bookingId") Long bookingId,
                                     @RequestParam("approved") Boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    /**
     * Отмена бронирования
     * Patch /bookings/bookingId/canceled
     * Headers X-Sharer-User-Id
     */
    @PatchMapping("/{bookingId}/canceled")
    public BookingResponseDto canceledBooking(@PathVariable("bookingId") Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.canceledBooking(userId, bookingId);
    }

    /**
     * Получение информации о бронировании.
     * Get /bookings/bookingId
     * Headers X-Sharer-User-Id
     */
    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@PathVariable("bookingId") Long bookingId,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(userId, bookingId);
    }

    /**
     * Получение списка броней с определенным состоянием,
     * Список отсортированы по дату в порядке убывания
     * GET /bookings?state={state}
     * Headers X-Sharer-User-Id
     */
    @GetMapping()
    public List<BookingResponseDto> getBookingsByState(@RequestParam(name = "state", defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingByState(userId, state);
    }

    /**
     * Получение списка броней всех вещей пользователя,
     * Список отсортированы по дату в порядке убывания
     * GET /bookings/owner?state={state}
     * Headers X-Sharer-User-Id
     */
    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsAllItemsByState(@RequestParam(name = "state", defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingsAllItemsByState(state, userId);
    }
}
