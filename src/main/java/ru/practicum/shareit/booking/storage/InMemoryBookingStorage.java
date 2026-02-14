package ru.practicum.shareit.booking.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;

@Repository
public class InMemoryBookingStorage {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private long idCounter = 1;

    public Booking create(Booking booking) {
        long id = idCounter++;
        booking.setId(id);
        bookings.put(id, booking);
        return booking;
    }

    public Booking update(Booking booking) {
        if (!bookings.containsKey(booking.getId())) {
            throw new NotFoundException("Бронирование не найдено");
        }

        bookings.put(booking.getId(), booking);
        return booking;
    }

    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(bookings.get(id));
    }
}
