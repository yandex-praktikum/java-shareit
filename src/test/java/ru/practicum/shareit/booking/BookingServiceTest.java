package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoState;
import ru.practicum.shareit.booking.dto.BookingDtoToUser;
import ru.practicum.shareit.booking.exception.BookingNotChangeStatusException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.exception.ItemNotAvalibleException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserIsNotOwnerException;
import ru.practicum.shareit.item.request.exception.ItemRequestNotGoodParametrsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.booking.BookingTestData.*;


@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    @Autowired
    private final BookingServiceImpl bookingService;


    @Test
    void testGetBookingById() {
        BookingDtoToUser bookingDtoFromSQL = bookingService.getBookingById(2L, 1L);
        bookingDtoFromSQL.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoFromSQL, equalTo(bookingDtoToUser1));
    }

    @Test
    void testGetBookingByIdTwo() {
        BookingDtoToUser bookingDtoFromSQL = bookingService.getBookingById(1L, 1L);
        bookingDtoFromSQL.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoFromSQL, equalTo(bookingDtoToUser1));
    }

    @Test
    void testGetBookingByIdNotBookerOrOwner() {
        assertThrows(UserIsNotOwnerException.class, () -> bookingService.getBookingById(3L, 1L));
    }

    @Test
    void testGetBookingByIdNotFound() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(3L, 10L));
    }

    @Test
    @DirtiesContext
    void testCreate() {
        long bookingId = bookingService.create(2L, 1L, createdDto).getId();
        BookingDtoToUser bookingDtoFromSQL = bookingService.getBookingById(2L, bookingId);
        bookingDtoFromSQL.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoFromSQL, equalTo(bookingDtoToUserCreated));
    }

    @Test
    void testCreateNotAvailable() {
        assertThrows(ItemNotAvalibleException.class, () -> bookingService.create(2L, 3L, createdDto));
    }

    @Test
    void testCreateWrongItem() {
        assertThrows(ItemNotFoundException.class, () -> bookingService.create(2L, 300L, createdDto));
    }

    @Test
    void testCreateOwner() {
        assertThrows(UserIsNotOwnerException.class, () -> bookingService.create(2L, 4L, createdDto));
    }

    @Test
    void testCreateWrongBooker() {
        assertThrows(UserNotFoundException.class, () -> bookingService.create(20L, 4L, createdDto));
    }

    @Test
    void testApproveStatus() {
        BookingDtoToUser bookingDtoFromSQL = bookingService.approveStatus(1L, 2L, true);
        bookingDtoFromSQL.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoFromSQL, equalTo(bookingDtoToUser2));
    }

    @Test
    void testApproveStatusRejected() {
        bookingDtoToUser2.setStatus(Status.REJECTED);
        BookingDtoToUser bookingDtoFromSQL = bookingService.approveStatus(1L, 2L, false);
        bookingDtoFromSQL.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoFromSQL, equalTo(bookingDtoToUser2));
        bookingDtoToUser2.setStatus(Status.APPROVED);
    }

    @Test
    void testApproveStatusNotChangeStatus() {
        assertThrows(BookingNotChangeStatusException.class, () -> bookingService.approveStatus(1L, 1L, true));
    }

    @Test
    void testApproveStatusNotOwner() {
        assertThrows(UserIsNotOwnerException.class, () -> bookingService.approveStatus(2L, 2L, true));
    }

    @Test
    void testApproveStatusBookingNotFound() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.approveStatus(1L, 20L, true));
    }


    @Test
    void testGetBookingCurrentUser() {
        List<BookingDtoState> bookings = bookingService.getBookingCurrentUser(2L, State.PAST, 0, 10);
        assertThat(bookings, equalTo(List.of(bookingDtoState)));
    }

    @Test
    void testGetBookingWrongCurrentUser() {
        assertThrows(UserNotFoundException.class, () -> bookingService
                .getBookingCurrentUser(200L, State.PAST, 0, 10));
    }

    @Test
    void testGetBookingCurrentUserWrongFrom() {
        assertThrows(ItemRequestNotGoodParametrsException.class, () -> bookingService
                .getBookingCurrentUser(2L, State.PAST, -1, 10));
    }

    @Test
    void testGetBookingCurrentOwner() {
        List<BookingDtoState> bookings = bookingService.getBookingCurrentOwner(1L, State.PAST, 0, 10);
        assertThat(bookings, equalTo(List.of(bookingDtoState)));
    }

    @Test
    void testGetBookingWrongCurrentOwner() {
        assertThrows(UserNotFoundException.class, () -> bookingService
                .getBookingCurrentOwner(100L, State.PAST, 0, 10));

    }

    @Test
    void testGetBookingCurrentOwnerWrongFrom() {
        assertThrows(ItemRequestNotGoodParametrsException.class, () ->
                bookingService.getBookingCurrentOwner(1L, State.PAST, -1, 10));

    }
}
