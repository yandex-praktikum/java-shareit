package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus()
        );
    }

    public static BookingDto toBookingDtoWithDetails(Booking booking, User booker, Item item) {
        BookingDto.BookerDto bookerDto = new BookingDto.BookerDto(booker.getId(), booker.getName());
        BookingDto.ItemDto itemDto = new BookingDto.ItemDto(item.getId(), item.getName());

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItemId(),
                booking.getBookerId(),
                booking.getStatus(),
                bookerDto,
                itemDto
        );
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItemId(),
                bookingDto.getBookerId(),
                bookingDto.getStatus()
        );
    }
}