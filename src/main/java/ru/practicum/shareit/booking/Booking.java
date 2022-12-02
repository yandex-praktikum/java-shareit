package ru.practicum.shareit.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
public class Booking {

    int bookingId;

    int itemId;

    LocalDate start;

    LocalDate end;

    boolean confirmation;

    String review;
}
