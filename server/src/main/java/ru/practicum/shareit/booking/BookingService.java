package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.StatusDto;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingService {
    BookingDto add(BookingDto bookingDto, Integer ownerId);

    BookingDto updApprove(Integer bookingId, Boolean approved, Integer userId);

    BookingDto findById(Integer bookingId, Integer userId);

    Collection<BookingDto> findAllByUser(Integer userId, StatusDto state, Integer page, Integer size);

    Collection<BookingDto> findAllByOwner(Integer userId, StatusDto state, Integer page, Integer size);

    void updBookingDate(Integer id, LocalDateTime dateTime);

}
