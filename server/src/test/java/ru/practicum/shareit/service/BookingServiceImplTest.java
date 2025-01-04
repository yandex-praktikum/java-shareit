package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.util.enums.BookerStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.util.enums.ItemStatus;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {
    private final BookingServiceImpl bookingService;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    User owner;
    User booker;
    Item item;
    Booking currentBooking;
    Booking futureBooking;
    Booking pastBooking;

    @BeforeEach
    public void setUp() {
        owner = new User(null, "Owner-1", "owner@example.com");
        booker = new User(null, "Booker-1", "booker@example.com");
        item = new Item(null, "Item-1", "Item-1-description", ItemStatus.AVAILABLE, owner, null);

        LocalDateTime dateTimeNow = LocalDateTime.now();

        currentBooking = new Booking(
                null, dateTimeNow.minusDays(7),
                dateTimeNow.plusDays(3), item,
                booker, BookerStatus.APPROVED);

        futureBooking = new Booking(
                null, dateTimeNow.plusDays(4),
                dateTimeNow.plusDays(6), item,
                booker, BookerStatus.APPROVED);

        pastBooking = new Booking(
                null, dateTimeNow.minusDays(10),
                dateTimeNow.minusDays(9), item,
                booker, BookerStatus.WAITING);
    }

    @Test
    public void getAllByUserId() {
        userRepository.save(owner);
        booker = userRepository.save(booker);
        itemRepository.save(item);

        currentBooking = bookingRepository.save(currentBooking);
        futureBooking = bookingRepository.save(futureBooking);
        pastBooking = bookingRepository.save(pastBooking);

        List<List<Booking>> bookings = List.of(
                List.of(currentBooking, futureBooking, pastBooking),
                List.of(currentBooking),
                List.of(futureBooking),
                List.of(pastBooking)
        );

        List<String> states = List.of("ALL", "CURRENT", "FUTURE", "PAST");

        IntStream.range(0, states.size()).forEach(index -> {
            List<BookingDto> foundBookings = new ArrayList<>(bookingService.getAllByUserId(booker.getId(), states.get(index)));
            List<Booking> expectedBookings = bookings.get(index);
            assertThat(foundBookings).isNotNull();
            assertThat(foundBookings).isNotEmpty();
            assertThat(foundBookings.size()).isEqualTo(expectedBookings.size());
            assertThat(collectionsWithBookingsAreEqual(foundBookings, expectedBookings)).isTrue();
        });

    }

    private boolean collectionsWithBookingsAreEqual(List<BookingDto> bookings1, List<Booking> bookings2) {
        if (bookings1.size() != bookings2.size())
            return false;

        for (Booking booking : bookings2)
            if (bookings1.stream().filter(
                    i -> i.getId().equals(booking.getId())).findAny().isEmpty())
                return false;

        return true;
    }
}
