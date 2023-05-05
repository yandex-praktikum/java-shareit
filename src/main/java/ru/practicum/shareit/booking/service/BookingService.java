package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(int userId, BookingDto bookingDto);

    Booking approvedBooking(int userId, int bookingId, boolean isApproved);

    Booking findBookingById(int userId, int bookingId);

    List<Booking> findBookingByUserId(int userId, String state,  int from, int size);

    List<Booking> findBookingByOwnerId(int userId, String state,  int from, int size);
}
