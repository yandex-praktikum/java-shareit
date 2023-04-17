package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {
    private static BookingDto inBookingDto(Booking booking){
        return new BookingDto(
                booking.getItemId(),
                booking.getUserBookedId(),
                booking.getStatus(),
                booking.getStartTime(),
                booking.getEndTime()
        );
    }
}
