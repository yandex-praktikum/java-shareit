package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long bookerId);

    BookingDto approveBooking(Long bookingId, Long ownerId, boolean approved);

    BookingDto getBookingById(Long bookingId, Long userId);

    BookingDto cancelBooking(Long bookingId, Long userId);

    List<BookingDto> getBookingsByBooker(Long bookerId, String state);

    List<BookingDto> getBookingsByOwner(Long ownerId, String state);
}
