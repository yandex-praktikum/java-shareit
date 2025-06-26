package shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DataJpaTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = ShareItServer.class)
public class BookingRepositoryTest {

    @Autowired
    private ItemRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private final LocalDateTime testTime = LocalDateTime.of(2025, 07, 25, 00, 00, 00);

    private final User user1 = User.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final User user2 = User.builder()
            .id(2L)
            .name("user2")
            .email("email2@ya.ru")
            .build();

    private final Item item1 = Item.builder()
            .id(1L)
            .name("item1")
            .description("description1")
            .available(true)
            .owner(user1)
            .build();

    private final Item item2 = Item.builder()
            .id(2L)
            .name("item2")
            .description("description2")
            .available(true)
            .owner(user2)
            .build();

    private final Item item3 = Item.builder()
            .id(3L)
            .name("name3")
            .description("description3")
            .available(false)
            .owner(user1)
            .build();

    private final Booking bookingPast = Booking.builder()
            .start(testTime.minusYears(10))
            .end(testTime.minusYears(9))
            .item(item1)
            .booker(user1)
            .status(BookingStatus.APPROVED)
            .build();

    private final Booking bookingCurrent = Booking.builder()
            .start(testTime.minusYears(5))
            .end(testTime.plusYears(5))
            .item(item1)
            .booker(user2)
            .status(BookingStatus.APPROVED)
            .build();

    private final Booking bookingFuture = Booking.builder()
            .start(testTime.plusYears(8))
            .end(testTime.plusYears(9))
            .item(item1)
            .booker(user2)
            .status(BookingStatus.APPROVED)
            .build();

    private final Booking bookingRejected = Booking.builder()
            .start(testTime.plusYears(9))
            .end(testTime.plusYears(10))
            .item(item1)
            .booker(user1)
            .status(BookingStatus.REJECTED)
            .build();

    private void checkBooking(Booking booking1, Booking booking2) {
        assertEquals(booking1.getId(), booking2.getId());
        assertEquals(booking1.getStart(), booking2.getStart());
        assertEquals(booking1.getEnd(), booking2.getEnd());
        assertEquals(booking1.getStatus(), booking2.getStatus());

        assertEquals(booking1.getBooker().getId(), booking2.getBooker().getId());
        assertEquals(booking1.getBooker().getName(), booking2.getBooker().getName());

        assertEquals(booking1.getItem().getId(), booking2.getItem().getId());
        assertEquals(booking1.getItem().getName(), booking2.getItem().getName());
    }

    @BeforeEach
    public void init() {
        userRepository.save(user1);
        userRepository.save(user2);
        repository.save(item1);
        repository.save(item2);
        repository.save(item3);
        bookingRepository.save(bookingCurrent);
        bookingRepository.save(bookingFuture);
        bookingRepository.save(bookingRejected);
        bookingRepository.save(bookingPast);
    }

    @Test
    public void getBookingByStateAll() {
        List<Booking> bookingsUser = bookingRepository.getBookingByStateALL(user2.getId());

        log.info("Всего бронирований у user2: {}", bookingsUser.size());
        bookingsUser.forEach(b -> log.info("Booking id = {}, booker = {}", b.getId(), b.getBooker().getName()));

        assertEquals(2, bookingsUser.size(), "Количество бронирований пользователя не совпадает");
        checkBooking(bookingsUser.get(0), bookingFuture);
        checkBooking(bookingsUser.get(1), bookingCurrent);
    }

    @Test
    public void getBookingByStateStatus() {
        List<Booking> bookings = bookingRepository.getBookingByStateStatus(user2.getId(), BookingStatus.APPROVED);

        bookings.stream().forEach(booking -> log.info(booking.getId().toString()));
        assertEquals(2, bookings.size(), "Количество бронирований пользователя не совпадает");
        checkBooking(bookings.get(0), bookingFuture);
        checkBooking(bookings.get(1), bookingCurrent);
    }

    @Test
    public void getBookingByStateCurrent() {
        List<Booking> bookings = bookingRepository.getBookingByStateCurrent(user2.getId(), testTime);

        bookings.forEach(b -> log.info("Booking id = {}, booker = {}", b.getId(), b.getBooker().getName()));

        assertEquals(1, bookings.size(), "Количество бронирований пользователя не совпадает");
        checkBooking(bookings.get(0), bookingCurrent);
    }

    @Test
    public void getBookingByStateFuture() {
        List<Booking> bookings = bookingRepository.getBookingByStateFuture(user2.getId(), testTime);

        assertEquals(1, bookings.size(), "Количество бронирований пользователя не совпадает");
        checkBooking(bookings.get(0), bookingFuture);
    }

    @Test
    public void getBookingByStatePast() {
        List<Booking> bookings = bookingRepository.getBookingByStatePast(user1.getId(), testTime);

        assertEquals(1, bookings.size(), "Количество бронирований пользователя не совпадает");
        checkBooking(bookings.get(0), bookingPast);
    }

    @Test
    public void getBookingAllItemsByStateAll() {
        List<Booking> bookings = bookingRepository.getBookingAllItemsByStateALL(item1.getId());

        bookings.stream().forEach(booking -> log.info(booking.getId().toString()));
        assertEquals(4, bookings.size(), "Количество бронирований пользователя не совпадает");
        checkBooking(bookings.get(0), bookingRejected);
        checkBooking(bookings.get(1), bookingFuture);
        checkBooking(bookings.get(2), bookingCurrent);
        checkBooking(bookings.get(3), bookingPast);
    }

    @Test
    public void getBookingAllItemsByStateStatus() {
        List<Booking> bookings = bookingRepository.getBookingAllItemsByStateStatus(item1.getId(), BookingStatus.APPROVED);

        bookings.stream().forEach(booking -> log.info(booking.getId().toString()));
        assertEquals(3, bookings.size(), "Количество бронирований пользователя не совпадает");
        checkBooking(bookings.get(0), bookingFuture);
        checkBooking(bookings.get(1), bookingCurrent);
        checkBooking(bookings.get(2), bookingPast);
    }
}
