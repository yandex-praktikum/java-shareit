package ru.practicum.shareit.validator;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingJpaRepository;
import ru.practicum.shareit.item.repository.ItemJpaRepository;
import ru.practicum.shareit.user.repository.UserJpaRepository;

@Component
public class Validator {

    public boolean validateUser(int userId, UserJpaRepository userRepository) {
        return userRepository.findById(userId).isPresent();
    }

    public boolean validateItem(int itemId, ItemJpaRepository itemRepository) {
        return itemRepository.findById(itemId).isPresent();
    }

    public boolean validateBooking(int bookingId, BookingJpaRepository bookingRepository) {
        return bookingRepository.findById(bookingId).isPresent();
    }

    public boolean validateBookingAndItem(int bookingId, BookingJpaRepository bookingRepository,
                                          ItemJpaRepository itemRepository) {
        if (bookingRepository.findById(bookingId).isPresent()) {
            Booking booking = bookingRepository.findById(bookingId).get();
            return itemRepository.findById(booking.getItem().getId()).isPresent();
        } else {
            return false;
        }
    }

}
