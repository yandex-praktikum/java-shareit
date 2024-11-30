package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long item;
    private Long booker;
    private BookingStatus status;
}
