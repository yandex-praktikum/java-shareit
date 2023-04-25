package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingStatus;

import java.time.LocalDateTime;

public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long item;
    private Long booker;
    private BookingStatus status;
}