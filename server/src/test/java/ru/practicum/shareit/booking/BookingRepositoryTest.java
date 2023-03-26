package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookingRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookingRepository bookingRepository;

    private User bookerUser;
    private User ownerUser;
    private Item itemToBook;
    private Booking approvedBooking;
    private Booking waitingBooking;
    private Booking rejectedBooking;

    @BeforeEach
    void beforeEach() {
        bookerUser = em.persistAndFlush(User.builder().name("booker").email("booker@email.ru").build());
        ownerUser = em.persistAndFlush(User.builder().name("owner").email("owner@email.ru").build());
        itemToBook = em.persistAndFlush(Item.builder().name("компрессор").description("мощный компрессор")
                .available(true).ownerId(ownerUser.getId()).build());

        LocalDateTime nowHour = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        approvedBooking = em.persistAndFlush(Booking.builder().booker(bookerUser).item(itemToBook)
                .rentStartDate(nowHour.minusHours(3))
                .rentEndDate(nowHour.minusHours(2))
                .status(BookingStatus.APPROVED).build());
        waitingBooking = em.persistAndFlush(Booking.builder().booker(bookerUser).item(itemToBook)
                .rentStartDate(nowHour.plusHours(3))
                .rentEndDate(nowHour.plusHours(4))
                .status(BookingStatus.WAITING).build());
        rejectedBooking = em.persistAndFlush(Booking.builder().booker(bookerUser).item(itemToBook)
                .rentStartDate(nowHour.plusHours(1))
                .rentEndDate(nowHour.plusHours(2))
                .status(BookingStatus.REJECTED).build());
    }

    @Test
    void findByBooker_idOrderByRentStartDateDescTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository.findByBooker_idOrderByRentStartDateDesc(bookerUser.getId(), page);

        assertEquals(3, bookingList.size());
        assertEquals(waitingBooking.getId(), bookingList.get(0).getId());
        assertEquals(rejectedBooking.getId(), bookingList.get(1).getId());
        assertEquals(approvedBooking.getId(), bookingList.get(2).getId());
    }

    @Test
    void findByBooker_idAndStatusOrderByRentStartDateDescTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findByBooker_idAndStatusOrderByRentStartDateDesc(bookerUser.getId(), BookingStatus.REJECTED, page);

        assertEquals(1, bookingList.size());
        assertEquals(rejectedBooking.getId(), bookingList.get(0).getId());
    }

    @Test
    void findBookingsOfItemsByOwnerIdTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository.findBookingsOfItemsByOwnerId(ownerUser.getId(), page);

        assertEquals(3, bookingList.size());
        assertEquals(waitingBooking.getId(), bookingList.get(0).getId());
        assertEquals(rejectedBooking.getId(), bookingList.get(1).getId());
        assertEquals(approvedBooking.getId(), bookingList.get(2).getId());
    }

    @Test
    void findBookingsOfItemsByOwnerIdAndStatusTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findBookingsOfItemsByOwnerIdAndStatus(ownerUser.getId(), BookingStatus.APPROVED, page);

        assertEquals(1, bookingList.size());
        assertEquals(approvedBooking.getId(), bookingList.get(0).getId());
    }

    @Test
    void findBookingsOfItemsByOwnerIdInFutureTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findBookingsOfItemsByOwnerIdInFuture(ownerUser.getId(), LocalDateTime.now(), page);

        assertEquals(2, bookingList.size());
        assertEquals(waitingBooking.getId(), bookingList.get(0).getId());
        assertEquals(rejectedBooking.getId(), bookingList.get(1).getId());
    }

    @Test
    void findBookingsOfItemsByOwnerIdInPastTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findBookingsOfItemsByOwnerIdInPast(ownerUser.getId(), LocalDateTime.now(), page);

        assertEquals(1, bookingList.size());
        assertEquals(approvedBooking.getId(), bookingList.get(0).getId());
    }

    @Test
    void findBookingsOfItemsByOwnerIdInCurrentTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findBookingsOfItemsByOwnerIdInCurrent(ownerUser.getId(), LocalDateTime.now(), page);

        assertTrue(bookingList.isEmpty());
    }

    @Test
    void findByBooker_idAndRentStartDateAfterOrderByRentStartDateDescTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findByBooker_idAndRentStartDateAfterOrderByRentStartDateDesc(bookerUser.getId(), LocalDateTime.now(), page);

        assertEquals(2, bookingList.size());
        assertEquals(waitingBooking.getId(), bookingList.get(0).getId());
        assertEquals(rejectedBooking.getId(), bookingList.get(1).getId());
    }

    @Test
    void findByBooker_idAndRentStartDateBeforeAndRentEndDateAfterOrderByRentStartDateDescTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findByBooker_idAndRentStartDateBeforeAndRentEndDateAfterOrderByRentStartDateDesc(bookerUser.getId(),
                        LocalDateTime.now(), LocalDateTime.now(), page);
        assertTrue(bookingList.isEmpty());
    }

    @Test
    void findByBooker_idAndRentEndDateBeforeOrderByRentStartDateDescTest() {
        Pageable page = PageRequest.of(0, 20);
        List<Booking> bookingList = bookingRepository
                .findByBooker_idAndRentEndDateBeforeOrderByRentStartDateDesc(bookerUser.getId(), LocalDateTime.now(), page);

        assertEquals(1, bookingList.size());
        assertEquals(approvedBooking.getId(), bookingList.get(0).getId());
    }

    @Test
    void findByItem_idInTest() {
        List<Booking> bookingList = bookingRepository
                .findByItem_idIn(List.of(itemToBook.getId()));

        assertEquals(3, bookingList.size());
    }

    @Test
    void findByItem_idAndBooker_idAndStatusAndRentEndDateIsBefore() {
        List<Booking> bookingList = bookingRepository
                .findByItem_idAndBooker_idAndStatusAndRentEndDateIsBefore(itemToBook.getId(), bookerUser.getId(),
                        BookingStatus.APPROVED, LocalDateTime.now());

        assertEquals(1, bookingList.size());
        assertEquals(approvedBooking.getId(), bookingList.get(0).getId());
    }
}