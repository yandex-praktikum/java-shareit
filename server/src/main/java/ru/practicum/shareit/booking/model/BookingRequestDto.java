package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingRequestDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}