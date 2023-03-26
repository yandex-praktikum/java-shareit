package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Класс для отправки данных о предыдущем и следующем бронированиях вещи
 */
@Getter
@AllArgsConstructor
public class BookingShortDto {
    private long id;
    private long bookerId;
    private LocalDateTime rentStartTime;
    private LocalDateTime rentEndTime;
}