package ru.practicum.shareit.booking.model;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long id;

    @NotNull(message = "Start date and time cannot be null")
    @FutureOrPresent(message = "Start date and time must be in the present or future")
    private LocalDateTime start;

    @NotNull(message = "End date and time cannot be null")
    @FutureOrPresent(message = "End date and time must be in the present or future")
    private LocalDateTime end;

    @NotNull(message = "Item cannot be null")
    private Item item;

    @NotNull(message = "Booker cannot be null")
    private User booker;

    @NotNull(message = "Status cannot be null")
    private BookingStatus status;
}