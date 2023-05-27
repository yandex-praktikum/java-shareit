package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getDateBegin(),
                booking.getDateEnd(),
                booking.getItem().getId(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getBooker().getId(),
                booking.getStatus());
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(null,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                null,
                null,
                bookingDto.getStatus());
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookingList) {
        return bookingList.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
