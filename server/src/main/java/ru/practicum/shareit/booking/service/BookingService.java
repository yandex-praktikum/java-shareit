package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {
    Booking findBookingById(Long bookingId, Long userId);

    List<Booking> findAllBookingsByUser(Long userId, BookingState state);

    List<Booking> getOwnerBookings(Long ownerId, BookingState state);

    Booking createBooking(Booking dto, Long bookerId);

    Booking updateBookingStatus(Long bookingId, Long ownerId, boolean approved);
}