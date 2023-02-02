package ru.practicum.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.booking.Status;
import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingDtoResponse {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;
}
