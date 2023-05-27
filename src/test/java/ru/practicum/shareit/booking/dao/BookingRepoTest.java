package ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Date;


@DataJpaTest
public class BookingRepoTest {

    @Autowired
    private BookingRepository repository;

    @Test
    public void shouldGenerateIdOnSave() {
        Booking booking = new Booking();
        booking.setItem(new Item());
        booking.setStatus("APPROVED");
        booking.setDateBegin(new Date());
        booking.setDateEnd(new Date());
        booking.setBooker(new User());

        Assertions.assertNull(booking.getId());
        repository.save(booking);
        Assertions.assertNotNull(booking.getId());
    }

}
