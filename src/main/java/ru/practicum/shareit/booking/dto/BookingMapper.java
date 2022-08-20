package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mopel.User;

import java.time.LocalDateTime;

@Component
public class BookingMapper {

    public Booking toBooking(BookingDto bookingDto, User booker, Item item, Status status) {
        Long id = bookingDto.getId();
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();

        return new Booking(id, start, end, item, booker, status);
    }

    public BookingStatusDto toBookingDto(Booking booking) {
        Long id = booking.getId();
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        Status status = booking.getStatus();
        User booker = booking.getBooker();
        UserDto bookerDto = new UserDto(booker.getId(), booker.getName(), booker.getEmail());
        Item item = booking.getItem();
        ItemInputDto itemInputDto = new ItemInputDto(item.getId(), item.getName(),
                item.getDescription(),item.getAvailable());

        return new BookingStatusDto(id, start, end, status, bookerDto, itemInputDto);
    }

}
