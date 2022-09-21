package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDtoToUser create(long userId, long itemId, BookingDto bookingDto);

    BookingDtoToUser approveStatus(long userId, long bookingId, boolean approved);

    BookingDtoToUser getBookingById(long userId, long bookingId);

    List<BookingDtoState> getBookingCurrentUser(long userId, State stateEnum, int from, int size);

    List<BookingDtoState> getBookingCurrentOwner(long userId, State stateEnum, int from, int size);
}