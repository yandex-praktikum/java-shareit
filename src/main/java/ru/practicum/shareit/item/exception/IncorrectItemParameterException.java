package ru.practicum.shareit.item.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IncorrectItemParameterException extends RuntimeException {
    private final String parameter;
}
