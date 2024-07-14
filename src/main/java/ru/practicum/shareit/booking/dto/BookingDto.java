package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;

    @NotNull(message = "Start date and time cannot be null")
    @FutureOrPresent(message = "Start date and time must be in the present or future")
    private LocalDateTime start;

    @NotNull(message = "End date and time cannot be null")
    @FutureOrPresent(message = "End date and time must be in the present or future")
    private LocalDateTime end;

    @NotNull(message = "Item ID cannot be null")
    private Long itemId;

    @NotNull(message = "Booker ID cannot be null")
    private Long bookerId;

    @NotNull(message = "Status cannot be null")
    private BookingStatus status;
}