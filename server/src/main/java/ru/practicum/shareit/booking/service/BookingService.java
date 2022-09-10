package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingUpdDto;
import ru.practicum.shareit.booking.model.StateEnum;

import java.util.List;

public interface BookingService {
    List<BookingUpdDto> getAllByBookerId(StateEnum state, long userId, int from, int size);

    BookingUpdDto getBookingById(long bookingId, long userId);

    BookingUpdDto create(BookingCreateDto booking, long userId);

    void delete(long bookingId);

    BookingUpdDto update(boolean approved, long bookingId, long userId);

    List<BookingUpdDto> getAllWhereOwnerOfItems(StateEnum state, long ownerId, int from, int size);
}
