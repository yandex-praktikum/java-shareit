package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.Collection;

public interface BookingService {

    BookingDto addBooking(int userId, BookingSaveDto bookingSaveDto);

    BookingDto manageBooking(int userId, int bookingId, boolean approved);

    BookingDto getBooking(int userId, int bookingId);

    Collection<BookingDto> getAllUserBookings(int userId, BookingState state);

    Collection<BookingDto> getAllUserItemsBookings(int userId, BookingState state);
}
