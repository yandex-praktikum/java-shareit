package ru.practicum.shareit.booking.util.enums;

import ru.practicum.shareit.exception.ValidationException;

public enum State {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static State fromString(String stateString) {
        if (stateString != null) {
            try {
                return State.valueOf(stateString.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Обработка случая, когда строка не соответствует ни одному элементу перечисления
                throw new ValidationException(String.format("Нет соответствующего состояния для: %s", stateString));
            }
        }
        return null;
    }
}

