package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * Класс для данных для создания бронирования
 */
@Getter
@Builder
public class BookingCreateRequest {
    @Positive
    private long itemId;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}