package ru.practicum.shareit.booking.service;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {

    Booking map(BookingSaveDto booking);

    BookingDto map(Booking booking);

    Collection<BookingDto> map(Collection<Booking> bookings);
}
