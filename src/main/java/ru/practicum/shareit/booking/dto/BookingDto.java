package ru.practicum.shareit.booking.dto;

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
public class BookingDto {
    private Long itemId;
    private Long userBookedId;
    private String status;
    private LocalDate startTime;
    private LocalDate endTime;

}
