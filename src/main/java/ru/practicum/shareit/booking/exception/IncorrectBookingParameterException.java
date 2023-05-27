package ru.practicum.shareit.booking.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class IncorrectBookingParameterException extends RuntimeException {
    private final String parameter;
}
