package ru.practicum.shareit.item.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IncorrectParameterException extends RuntimeException {
    private final String parameter;
}
