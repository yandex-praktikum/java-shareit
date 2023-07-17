package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.Booking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BookingRepositoryImpl implements BookingRepository {
    final Map<Long, Booking> bookingMap = new HashMap<>();

    /**
     * Получить список всех бронирований.
     * @return список бронирований.
     */
    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookingMap.values());
    }

    /**
     * Получить бронирование по ID.
     * @param id ID брони.
     * @return запрашиваемое бронирование.
     */
    @Override
    public Booking getBookingById(Long id) {
        return bookingMap.get(id);
    }

    /**
     * Получить список бронирований пользователя с ID.
     * @param userId ID пользователя.
     * @return список бронирований.
     */
    @Override
    public List<Booking> getBookingsByUserId(Long userId) {

        return bookingMap.values().stream().filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    /**
     * Удалить бронирования пользователя с ID = userId.
     * @param userId ID пользователя.
     */
    @Override
    public void removeBookingsByUserId(Long userId) {
        List<Long> idForRemove = bookingMap.values().stream().filter(b -> b.getUserId().equals(userId))
                .map(Booking::getId).collect(Collectors.toList());
    }
}
