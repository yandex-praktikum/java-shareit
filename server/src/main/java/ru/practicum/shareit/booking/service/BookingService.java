package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.base.service.BaseService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestCreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService extends BaseService<Booking, Long> {
    BookingDto create(Long userId, RequestCreateBookingDto bookingDto);

    BookingDto approveBooking(Long userId, Long bookingId, boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    Collection<BookingDto> getAllByUserId(Long userId, String state);

    Collection<BookingDto> getAllByItemOwnerId(Long userId, String state);
}
