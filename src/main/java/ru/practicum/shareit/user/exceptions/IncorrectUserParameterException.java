package ru.practicum.shareit.user.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IncorrectUserParameterException extends RuntimeException {
    private final String parameter;
}
