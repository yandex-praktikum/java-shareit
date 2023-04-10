package ru.practicum.shareit.booking;

import lombok.Value;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class Booking {
    long id;

    @NotNull(message = "Booking start date cannot be null")
    @Future(message = "Booking start date must be in the future")
    LocalDateTime start;

    @NotNull(message = "Booking end date cannot be null")
    @Future(message = "Booking end date must be in the future")
    LocalDateTime end;

    @NotNull(message = "Item cannot be null")
    Item item;

    @NotNull(message = "Booker cannot be null")
    User booker;

    @NotNull(message = "Booking status cannot be null")
    BookingStatus status;
}
