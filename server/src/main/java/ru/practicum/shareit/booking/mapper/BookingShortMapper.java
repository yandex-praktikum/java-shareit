package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingShortDto;


public class BookingShortMapper {

    public static BookingShortDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingShortDto.builder()
                .itemId(booking.getId())
                .bookingId(booking.getBooker().getId())
                .build();
    }
}