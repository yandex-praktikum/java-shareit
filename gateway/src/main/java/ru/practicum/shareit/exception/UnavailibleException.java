package ru.practicum.shareit.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnavailibleException extends RuntimeException {
    public UnavailibleException(String message) {
        super(message);
    }
}
