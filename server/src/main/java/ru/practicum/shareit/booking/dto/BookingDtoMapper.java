package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.user.dto.UserDtoMapper;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingDtoMapper {
    public static BookingDto toBookingDto(Booking booking) {
        if (booking != null) {
            return BookingDto.builder()
                    .id(booking.getId())
                    .item(ItemDtoMapper.toItemDto(booking.getItem()))
                    .booker(UserDtoMapper.toUserDto(booking.getBooker()))
                    .start(booking.getRentStartDate())
                    .end(booking.getRentEndDate())
                    .status(booking.getStatus()).build();
        } else return null;
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        if (booking != null) {
            return new BookingShortDto(booking.getId(), booking.getBooker().getId(), booking.getRentStartDate(), booking.getRentEndDate());
        } else return null;
    }

    public static List<BookingDto> toBookingDtoList(List<Booking> bookingList) {
        List<BookingDto> bookingDtoList = new ArrayList<>();

        if (bookingList != null) {
            for (Booking booking : bookingList) {
                bookingDtoList.add(toBookingDto(booking));
            }
        }

        return bookingDtoList;
    }
}