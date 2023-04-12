package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Setter
@Getter
@AllArgsConstructor
public class Booking {
    private Long id;
    private Long itemId;
    private Long userBookedId;
    private String status;
    private LocalDate startTime;
    private LocalDate endTime;


}
