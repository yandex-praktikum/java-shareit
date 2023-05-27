package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> getBooking(String state, Integer bookerId);

    List<BookingDto> ownerItemsBooking(String state, Integer ownerId);

    BookingDto getBooking(Integer bookerId, Integer bookingId);

    BookingDto booking(Integer bookerId, BookingDto bookingDto);

    BookingDto aprove(Integer ownerId, Integer bookingId, boolean approved);
}
