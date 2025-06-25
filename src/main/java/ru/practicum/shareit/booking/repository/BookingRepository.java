package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingRepository {

    Booking save(Booking booking);

    Booking update(long Id, Booking booking);


    void del(long itemId);


    Booking getById(long id);

    Collection<Booking> findAll();


}
