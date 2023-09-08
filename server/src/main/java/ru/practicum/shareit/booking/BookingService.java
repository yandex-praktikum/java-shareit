package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto update(Long bookingId, Boolean approved, Long ownerId);

    BookingDto get(Long bookingId, Long userId);

    List<BookingDto> getAllForUser(Long userId, BookingState state, Integer from, Integer size);

    List<BookingDto> getAllForOwner(Long ownerId, BookingState state, Integer from, Integer size);

}
