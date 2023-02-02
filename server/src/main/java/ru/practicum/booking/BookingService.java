package ru.practicum.booking;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {

    BookingDtoResponse getById(Long bookingId, Long userId);

    List<BookingDtoResponse> getByUserId(Long ownerId, String state, Integer from, Integer size);

    BookingDtoResponse create(Long userId, BookingDto bookingDto);

    BookingDtoResponse update(Long userId, Long bookingId, Boolean newStatus);

    List<BookingDtoResponse> getByOwnerId(Long ownerId, String state, Integer from, Integer size);
}
