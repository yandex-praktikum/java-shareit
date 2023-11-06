package ru.practicum.shareit.booking;

import java.util.Optional;

public enum BookingState {
    ALL,
    REJECTED,
    FUTURE,
    PAST,
    CURRENT,
    WAITING,
    APPROVED;

    public static Optional<BookingState> from(String state) {
        switch (state) {
            case "ALL":
                return Optional.of(BookingState.ALL);
            case "CURRENT":
                return Optional.of(BookingState.CURRENT);
            case "PAST":
                return Optional.of(BookingState.PAST);
            case "FUTURE":
                return Optional.of(BookingState.FUTURE);
            case "WAITING":
                return Optional.of(BookingState.WAITING);
            case "REJECTED":
                return Optional.of(BookingState.REJECTED);
            case "APPROVED":
                return Optional.of(BookingState.APPROVED);
            default:
                return Optional.empty();
        }
    }

}
