package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@UtilityClass
public class BookingMapper {
    public Booking convertToBooking(BookingDto bookingDto) {

        return Booking.builder()
                .id(bookingDto.getId())
                .startDate(bookingDto.getStart())
                .endDate(bookingDto.getEnd())
                .build();
    }

    public BookingDto convertToBookingDto(Booking booking) {

        return BookingDto.builder()
                .id(booking.getId())
                .item(ItemMapper.convertToItem(booking.getItem()))
                .booker(UserMapper.converFromDto(booking.getBooker()))
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(booking.getBookingStatus())
                .build();
    }

    public BookingShortDto convertToBookingShortDto(Booking booking) {

        return BookingShortDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .build();

    }
}
