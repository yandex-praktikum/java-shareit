package ru.practicum.shareit.booking.dto.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestCreateBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;

import java.util.Collection;

public class BookingMapper {
    public static Booking fromDto(RequestCreateBookingDto bookingDto) {
        return new Booking(null, bookingDto.getStart(), bookingDto.getEnd(), null, null, null);
    }

    public static BookingDto toDto(Booking booking) {
        ItemDto itemDto = ItemMapper.toItemDto(booking.getItem());
        UserDto userDto = UserMapper.toUserDto(booking.getBooker());

        return new BookingDto(booking.getId(), booking.getStartDateTime(), booking.getEndDateTime(),
                itemDto, userDto, booking.getStatus());
    }

    public static Collection<BookingDto> toDto(Collection<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toDto).toList();
    }
}
