package ru.practicum.shareit;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Review;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
public class MainData {
    private final List<User> users = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final List<ItemRequest> requests = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final List<Review> reviews = new ArrayList<>();
}
