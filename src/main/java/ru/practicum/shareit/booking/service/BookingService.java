package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {

    Booking saveBooking(long userId, BookingDto bookingDto);

    Booking updateBooking(long userId, long itemId, BookingDto bookingDto);


    void delBooking(long userId, long itemId);


    BookingDto geyBookingById(long id);


    Collection<BookingDto> findAllBooking();
}
