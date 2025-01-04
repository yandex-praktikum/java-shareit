package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RequestCreateBookingDto {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
