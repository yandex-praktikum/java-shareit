package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, Long bookerId);

    BookingDto updateBooking(Long bookingId, BookingDto bookingDto);

    BookingDto getBookingById(Long bookingId);
}
