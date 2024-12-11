package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum BookingState {
    WAITING,
    REJECTED,
    CURRENT,
    PAST,
    FUTURE,
    ALL;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
