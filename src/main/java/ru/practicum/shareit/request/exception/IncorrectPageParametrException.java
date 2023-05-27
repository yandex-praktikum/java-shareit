package ru.practicum.shareit.request.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IncorrectPageParametrException extends RuntimeException {
    private final String message;
}
