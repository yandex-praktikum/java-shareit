package ru.practicum.shareit.bookingTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserState;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        classes = ShareItServer.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createBooking_shouldSaveAndReturnBooking() {
        User owner = User.builder()
                .name("Владелец")
                .email("owner@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        User booker = User.builder()
                .name("Арендатор")
                .email("booker@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(booker);

        Item item = new Item();
        item.setName("Дрель");
        item.setDescription("Аккумуляторная дрель");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(3));

        Booking result = bookingService.createBooking(booking, booker.getId());

        assertThat(result.getId(), notNullValue());
        assertThat(result.getItem().getId(), is(item.getId()));
        assertThat(result.getBooker().getId(), is(booker.getId()));
        assertThat(result.getStatus(), is(BookingStatus.WAITING));

        Booking savedBooking = bookingRepository.findById(result.getId()).orElse(null);
        assertThat(savedBooking, notNullValue());
        assertThat(savedBooking.getStatus(), is(BookingStatus.WAITING));
    }

    @Test
    void findBookingById_shouldReturnBooking() {
        User owner = User.builder()
                .name("Владелец2")
                .email("owner2@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        User booker = User.builder()
                .name("Арендатор2")
                .email("booker2@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(booker);

        Item item = new Item();
        item.setName("Перфоратор");
        item.setDescription("Мощный перфоратор");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(3));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);

        Booking result = bookingService.findBookingById(booking.getId(), booker.getId());

        assertThat(result.getId(), is(booking.getId()));
        assertThat(result.getItem().getId(), is(item.getId()));
        assertThat(result.getBooker().getId(), is(booker.getId()));
    }

    @Test
    void updateBookingStatus_shouldApproveBooking() {
        User owner = User.builder()
                .name("Владелец3")
                .email("owner3@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        User booker = User.builder()
                .name("Арендатор3")
                .email("booker3@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(booker);

        Item item = new Item();
        item.setName("Болгарка");
        item.setDescription("УШМ");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(3));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);

        Booking result = bookingService.updateBookingStatus(booking.getId(), owner.getId(), true);

        assertThat(result.getStatus(), is(BookingStatus.APPROVED));

        Booking updatedBooking = bookingRepository.findById(booking.getId()).orElse(null);
        assertThat(updatedBooking.getStatus(), is(BookingStatus.APPROVED));
    }

    @Test
    void updateBookingStatus_shouldRejectBooking() {
        User owner = User.builder()
                .name("Владелец4")
                .email("owner4@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        User booker = User.builder()
                .name("Арендатор4")
                .email("booker4@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(booker);

        Item item = new Item();
        item.setName("Лобзик");
        item.setDescription("Электрический лобзик");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(3));
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);

        Booking result = bookingService.updateBookingStatus(booking.getId(), owner.getId(), false);

        assertThat(result.getStatus(), is(BookingStatus.REJECTED));

        Booking updatedBooking = bookingRepository.findById(booking.getId()).orElse(null);
        assertThat(updatedBooking.getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void findAllBookingsByUser_shouldReturnUserBookings() {
        User owner = User.builder()
                .name("Владелец5")
                .email("owner5@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(owner);

        User booker = User.builder()
                .name("Арендатор5")
                .email("booker5@mail.com")
                .state(UserState.ACTIVE)
                .build();
        userRepository.save(booker);

        Item item = new Item();
        item.setName("Шуруповерт");
        item.setDescription("Аккумуляторный");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Booking booking1 = new Booking();
        booking1.setStart(LocalDateTime.now().plusDays(1));
        booking1.setEnd(LocalDateTime.now().plusDays(2));
        booking1.setItem(item);
        booking1.setBooker(booker);
        booking1.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking1);

        Booking booking2 = new Booking();
        booking2.setStart(LocalDateTime.now().plusDays(3));
        booking2.setEnd(LocalDateTime.now().plusDays(5));
        booking2.setItem(item);
        booking2.setBooker(booker);
        booking2.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking2);

        List<Booking> result = bookingService.findAllBookingsByUser(booker.getId(), BookingState.ALL);

        assertThat(result, hasSize(2));
    }
}