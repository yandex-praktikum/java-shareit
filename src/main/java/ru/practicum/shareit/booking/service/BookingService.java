package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;

import java.util.List;

public interface BookingService {
    List<BookingDto> getBooking(BookingStatus state, Integer bookerId, Integer from, Integer size);

    List<BookingDto> ownerItemsBookingLists(BookingStatus state, Integer ownerId, Integer from, Integer size);

    BookingDto getBooking(Integer bookerId, Integer bookingId);

    BookingDto booking(Integer bookerId, BookingDto bookingDto);

    BookingDto aprove(Integer ownerId, Integer bookingId, boolean approved);
}
