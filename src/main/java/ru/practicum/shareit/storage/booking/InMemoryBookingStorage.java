package ru.practicum.shareit.storage.booking;

import ru.practicum.shareit.booking.Booking;

import java.util.HashMap;

public class InMemoryBookingStorage implements BookingStorage {
    private HashMap<Integer, Booking> bookingHashMap = new HashMap<>();
    private Integer generatedBookingId;
}
