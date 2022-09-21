package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public interface BookingForItem {
    long getId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    Item getItem();

    User getBooker();
}
