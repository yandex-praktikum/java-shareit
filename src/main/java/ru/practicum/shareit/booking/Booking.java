package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
public class Booking {
    @NonNull
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private Long bookerId;
    private Status status;
}
