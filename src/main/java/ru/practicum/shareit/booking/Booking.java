package ru.practicum.shareit.booking;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @Positive
    private Long item; // id вещи
    @Positive
    private Long booker; // id пользователя, который осуществляет бронирование
    @NotNull
    private BookingStatus status;
}
