package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;

import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional
    BookingFullDto create(BookingDto bookingDto, Long userId);

    @Transactional
    BookingFullDto update(String approved, Long bookingId, Long userId);

    BookingFullDto get(Long userId, Long bookingId);

    List<BookingFullDto> getByUserIdAndState(Long userId, BookingState state);

    List<BookingFullDto> getAllBookingByItemsForUserId(Long userId, BookingState state, Long from, Long size);

    List<BookingFullDto> getAllByUserId(Long userId, Long from, Long size);
}
