package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus() != null ? booking.getStatus() : BookingStatus.WAITING
        );
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User booker) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                booker,
                bookingDto.getStatus() != null ? bookingDto.getStatus() : BookingStatus.WAITING
        );
    }

    public static ResponseBookingDto toResponseBookingDto(Booking booking) {
        return new ResponseBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus()
        );
    }

    public static List<ResponseBookingDto> listToResponseBookingDto(Iterable<Booking> bookings) {
        List<ResponseBookingDto> dtos = new ArrayList<>();
        for (Booking booking : bookings) {
            dtos.add(toResponseBookingDto(booking));
        }
        return dtos;
    }
}
