package ru.practicum.shareit.request.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ItemRequestNotFoundException extends RuntimeException {
    private final String message;
}
