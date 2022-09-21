package ru.practicum.shareit.booking.validation;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.exception.BookingDtoBadStateException;
import ru.practicum.shareit.booking.model.State;

@UtilityClass
public class ValidateState {
    public static State validateStatus(String text) {
        State stateEnum;
        try {
            stateEnum = State.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BookingDtoBadStateException("Unknown state: UNSUPPORTED_STATUS");
        }
        return stateEnum;
    }
}
