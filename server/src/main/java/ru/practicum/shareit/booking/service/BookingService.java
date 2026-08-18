package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;


public interface BookingService {

    BookingDto create(Long userId, BookingRequestDto bookingRequestDto);

    BookingDto approve(Long ownerId, Long bookingId, Boolean approved);

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBooker(Long userId, BookingState state);

    List<BookingDto> getBookingsByOwner(Long ownerId, BookingState state);
}