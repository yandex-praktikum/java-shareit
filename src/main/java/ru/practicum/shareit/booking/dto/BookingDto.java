package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Integer itemId;
    private Item item;

    private Integer userId;
    private User booker;

    private Status status;
}
