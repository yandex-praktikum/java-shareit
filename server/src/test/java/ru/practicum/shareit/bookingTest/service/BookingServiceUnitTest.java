package ru.practicum.shareit.bookingTest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceUnitTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBooking_shouldSaveAndReturnBooking() {
        User booker = new User();
        booker.setId(2L);

        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(owner);

        Booking bookingToSave = new Booking();
        bookingToSave.setStart(LocalDateTime.now().plusDays(1));
        bookingToSave.setEnd(LocalDateTime.now().plusDays(3));
        bookingToSave.setItem(item);

        Booking savedBooking = new Booking();
        savedBooking.setId(10L);
        savedBooking.setStart(bookingToSave.getStart());
        savedBooking.setEnd(bookingToSave.getEnd());
        savedBooking.setItem(item);
        savedBooking.setBooker(booker);
        savedBooking.setStatus(BookingStatus.WAITING);

        when(userService.getUserById(2L)).thenReturn(booker);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(savedBooking);

        Booking result = bookingService.createBooking(bookingToSave, 2L);

        assertThat(result.getId(), is(10L));
        assertThat(result.getItem().getId(), is(1L));
        assertThat(result.getBooker().getId(), is(2L));
        assertThat(result.getStatus(), is(BookingStatus.WAITING));
    }

    @Test
    void createBooking_shouldThrowWhenItemNotAvailable() {
        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(false);

        Booking booking = new Booking();
        booking.setItem(item);

        when(userService.getUserById(2L)).thenReturn(booker);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(booking, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Вещь недоступна для бронирования");
    }

    @Test
    void createBooking_shouldThrowWhenOwnerBooksOwnItem() {
        User booker = new User();
        booker.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(booker);

        Booking booking = new Booking();
        booking.setItem(item);

        when(userService.getUserById(1L)).thenReturn(booker);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(booking, 1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Владелец не может забронировать свою вещь");
    }

    @Test
    void createBooking_shouldThrowWhenStartAfterEnd() {
        User booker = new User();
        booker.setId(2L);

        Item item = new Item();
        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(new User());
        item.getOwner().setId(1L);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().plusDays(3));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        when(userService.getUserById(2L)).thenReturn(booker);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> bookingService.createBooking(booking, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Дата начала должна быть раньше даты окончания");
    }

    @Test
    void findBookingById_shouldReturnBooking_whenUserIsOwner() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setItem(item);

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        Booking result = bookingService.findBookingById(10L, 1L);

        assertThat(result.getId(), is(10L));
    }

    @Test
    void findBookingById_shouldThrowNotFound_whenBookingNotExists() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.findBookingById(999L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateBookingStatus_shouldApprove_whenOwnerApproves() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.updateBookingStatus(10L, 1L, true);

        assertThat(result.getStatus(), is(BookingStatus.APPROVED));
    }

    @Test
    void updateBookingStatus_shouldReject_whenOwnerRejects() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.updateBookingStatus(10L, 1L, false);

        assertThat(result.getStatus(), is(BookingStatus.REJECTED));
    }

    @Test
    void updateBookingStatus_shouldThrowAccessDenied_whenNotOwner() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setItem(item);

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.updateBookingStatus(10L, 2L, true))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("Статус может менять только владелец вещи.");
    }

    @Test
    void updateBookingStatus_shouldThrowWhenStatusNotWaiting() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setOwner(owner);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.updateBookingStatus(10L, 1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }
}