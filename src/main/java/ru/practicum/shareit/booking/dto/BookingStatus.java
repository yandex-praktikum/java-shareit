package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BookingStatus {
    CURRENT("CURRENT"),
    FUTURE("FUTURE"),
    PAST("PAST"),
    WAITING("WAITING"),
    REJECTED("REJECTED"),
    ALL("ALL");

    public final String label;
}
