package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    @NotNull(message = "Дата начала бронирования обязательна")
    @FutureOrPresent(message = "Дата начала должна быть в будущем")
    private LocalDateTime start;

    @NotNull(message = "Дата окончания бронирования обязательна")
    @Future(message = "Дата окончания бронирования должна быть в будущем")
    private LocalDateTime end;

    @NotNull(message = "ID вещи обязателен")
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;
}
