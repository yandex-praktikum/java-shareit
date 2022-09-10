package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class BookingMapper {
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private UserMapper userMapper;

    public BookingDto toBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setBookerId(booking.getBooker().getId());
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setItemId(booking.getItem().getId());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public Booking toBookingFromCreatedDto(BookingCreateDto bookingDto, long userId) {
        Booking booking = new Booking();
        booking.setBooker(userRepository.findById(userId).orElseThrow(UserNotFoundException::new));
        booking.setItem(itemRepository.findById(bookingDto.getItemId()).orElseThrow(ItemNotFoundException::new));
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        booking.setCreated(bookingDto.getCreated());
        return booking;
    }

    public BookingUpdDto toBookingUpdDto(Booking booking) {
        BookingUpdDto bookingUpdDto = new BookingUpdDto();
        bookingUpdDto.setId(booking.getId());
        bookingUpdDto.setStart(booking.getStart());
        bookingUpdDto.setEnd(booking.getEnd());
        bookingUpdDto.setStatus(booking.getStatus());
        bookingUpdDto.setBooker(userMapper.toUserDto(booking.getBooker()));
        bookingUpdDto.setItem(ItemMapper.toItemDto(booking.getItem()));

        return bookingUpdDto;
    }

    public List<BookingUpdDto> toBookingUpdDtoList(List<Booking> list) {
        return list.stream().map(this::toBookingUpdDto).collect(Collectors.toList());
    }

}
