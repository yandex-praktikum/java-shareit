package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatusDto;

import java.util.List;

public interface BookingService {

    BookingStatusDto create(Long bookerId, BookingDto bookingDto);

    BookingStatusDto approve(Long owner, Long bookingId, boolean approved);

    BookingStatusDto getById(Long userId, Long bookingId);

    List<BookingStatusDto> getByBookerId(Long bookerId, String state);

    List<BookingStatusDto> getByOwnerId(Long ownerId, String state);

}
