package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    Long id;
    @NotNull
    Long itemId;
    Long bookerId;
    Long itemOwnerId;
    @Future
    LocalDateTime start;
    @Future
    LocalDateTime end;
    BookingStatus status;
}
