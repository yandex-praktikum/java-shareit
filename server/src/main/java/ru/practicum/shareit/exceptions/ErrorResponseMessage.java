package ru.practicum.shareit.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorResponseMessage {
    private final String error;
    private final String message;
}
