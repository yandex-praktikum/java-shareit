package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.exceptions.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exception.PaginationException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    BookingRepository bookingRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    BookingServiceImpl bookingService;

    @Test
    void addBooking_correct() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 15, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);
        ResponseBookingDto test = BookingMapper.toResponseBookingDto(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        when(bookingRepository.save(any())).thenReturn(booking1);

        ResponseBookingDto result = bookingService.addBooking(bookingDto, 1);

        Assertions.assertEquals(test.getBooker().getId(), result.getBooker().getId());
        Assertions.assertEquals(test.getStart(), result.getStart());
    }

    @Test
    void addBooking_BookingForBooker() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 15, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);

        when(userRepository.findById(3)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(BookingNotFoundException.class, () -> bookingService.addBooking(bookingDto, 3));
    }

    @Test
    void addBooking_ItemIsNotAvailable() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", false, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 15, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(ItemIsNotAvailableException.class, () -> bookingService.addBooking(bookingDto, 1));
    }

    @Test
    void addBooking_ItemNotFoundException() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", false, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 15, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ItemNotFoundException.class, () -> bookingService.addBooking(bookingDto, 1));
    }

    @Test
    void addBooking_incorrectStart() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2023, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 15, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));

        Assertions.assertThrows(IncorrectBookingTimeException.class, () -> bookingService.addBooking(bookingDto, 1));
    }

    @Test
    void addBooking_incorrectEnd() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2023, 6, 10, 15, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Assertions.assertThrows(IncorrectBookingTimeException.class, () -> bookingService.addBooking(bookingDto, 1));
    }

    @Test
    void addBooking_equalEndAndStart() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 6, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Assertions.assertThrows(IncorrectBookingTimeException.class, () -> bookingService.addBooking(bookingDto, 1));
    }

    @Test
    void addBooking_EndAndStartSwitch() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.APPROVED);
        BookingDto bookingDto = BookingMapper.toBookingDto(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1)).thenReturn(Optional.of(item));
        Assertions.assertThrows(IncorrectBookingTimeException.class, () -> bookingService.addBooking(bookingDto, 1));
    }

    @Test
    void patchBooking_correct() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking bookingApproved = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.APPROVED);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        when(bookingRepository.save(any())).thenReturn(bookingApproved);

        ResponseBookingDto responseBookingDto = bookingService.patchBooking(3, 1, true);

        Assertions.assertEquals(BookingStatus.APPROVED, responseBookingDto.getStatus());
    }


    @Test
    void patchBooking_BookerIsTryingToCorrect() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.WAITING);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));

        Assertions.assertThrows(UserNotFoundException.class, () -> bookingService.patchBooking(1, 1, true));
    }

    @Test
    void patchBooking_IncorrectUserException() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.WAITING);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));

        Assertions.assertThrows(IncorrectUserException.class, () -> bookingService.patchBooking(2, 1, true));
    }

    @Test
    void patchBooking_AlreadyApproved() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.APPROVED);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));

        Assertions.assertThrows(ValidationException.class, () -> bookingService.patchBooking(3, 1, true));
    }

    @Test
    void patchBooking_rejected() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking rejected = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.REJECTED);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        when(bookingRepository.save(any())).thenReturn(rejected);

        ResponseBookingDto responseBookingDto = bookingService.patchBooking(3, 1, false);

        Assertions.assertEquals(BookingStatus.REJECTED, responseBookingDto.getStatus());
    }

    @Test
    void getBookingById_correct() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.WAITING);
        ResponseBookingDto test = BookingMapper.toResponseBookingDto(booking1);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));
        ResponseBookingDto result = bookingService.getBookingById(3, 1);

        Assertions.assertEquals(test.getItem().getName(), result.getItem().getName());
    }

    @Test
    void getBookingById_NotOwnerAndNotBooker() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 6, 10, 10, 0), item, user, BookingStatus.WAITING);
        ResponseBookingDto test = BookingMapper.toResponseBookingDto(booking1);

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking1));

        Assertions.assertThrows(NotOwnerAndNotBookerException.class, () -> bookingService.getBookingById(2, 1));
    }

    @Test
    void getAllUsersBookings_PaginationException() {
        User user = new User(1, "user", "user@mail.ru");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Assertions.assertThrows(PaginationException.class, () -> bookingService.getAllUsersBookings(1, State.ALL, -1, 2));
    }

    @Test
    void getAllUsersBookings_All() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2025, 7, 10, 10, 0),
                LocalDateTime.of(2025, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking1, booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(1, page)).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllUsersBookings(1, State.ALL, 0, 2);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    void getAllUsersBookings_CURRENT() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 3, 10, 10, 0),
                LocalDateTime.of(2025, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(eq(1), any(), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllUsersBookings(1, State.CURRENT, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getId());
    }

    @Test
    void getAllUsersBookings_PAST() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 3, 10, 10, 0),
                LocalDateTime.of(2024, 4, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(eq(1), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllUsersBookings(1, State.PAST, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getId());
    }

    @Test
    void getAllUsersBookings_FUTURE() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 3, 10, 10, 0),
                LocalDateTime.of(2024, 4, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(eq(1), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllUsersBookings(1, State.FUTURE, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
    }

    @Test
    void getAllUsersBookings_WAITING() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 9, 10, 10, 0),
                LocalDateTime.of(2024, 10, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking1, booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(eq(1), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllUsersBookings(1, State.WAITING, 0, 2);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
    }

    @Test
    void getAllUsersBookings_REJECTED() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 9, 10, 10, 0),
                LocalDateTime.of(2024, 10, 10, 10, 0), item, user, BookingStatus.REJECTED);
        List<Booking> bookings = List.of(booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(eq(1), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllUsersBookings(1, State.REJECTED, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getId());
    }

    @Test
    void getAllItemOwnerBookings_PaginationException() {
        User user = new User(1, "user", "user@mail.ru");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Assertions.assertThrows(PaginationException.class, () -> bookingService.getAllItemOwnerBookings(1, State.ALL, -1, 2));
    }

    @Test
    void getAllItemOwnerBookings_All() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2025, 7, 10, 10, 0),
                LocalDateTime.of(2025, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking1, booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(3)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(3, page)).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllItemOwnerBookings(3, State.ALL, 0, 2);

        Assertions.assertEquals(2, result.size());
    }

    @Test
    void getAllItemOwnerBookings_CURRENT() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 3, 10, 10, 0),
                LocalDateTime.of(2025, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(3)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(eq(3), any(), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllItemOwnerBookings(3, State.CURRENT, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getId());
    }

    @Test
    void getAllItemOwnerBookings_PAST() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 3, 10, 10, 0),
                LocalDateTime.of(2024, 4, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(3)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(eq(3), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllItemOwnerBookings(3, State.PAST, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getId());
    }

    @Test
    void getAllItemOwnerBookings_FUTURE() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 3, 10, 10, 0),
                LocalDateTime.of(2024, 4, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking1);

        when(userRepository.findById(3)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(eq(3), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllItemOwnerBookings(3, State.FUTURE, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
    }

    @Test
    void getAllItemOwnerBookings_WAITING() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 9, 10, 10, 0),
                LocalDateTime.of(2024, 10, 10, 10, 0), item, user, BookingStatus.WAITING);
        List<Booking> bookings = List.of(booking1, booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(3)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(eq(3), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllItemOwnerBookings(3, State.WAITING, 0, 2);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
    }

    @Test
    void getAllItemOwnerBookings_REJECTED() {
        User user = new User(1, "user", "user@mail.ru");
        User owner = new User(3, "owner", "owner@mail.ru");
        Item item = new Item(1, "item", "description", true, owner);
        Booking booking1 = new Booking(1, LocalDateTime.of(2024, 7, 10, 10, 0),
                LocalDateTime.of(2024, 8, 10, 10, 0), item, user, BookingStatus.WAITING);
        Booking booking2 = new Booking(2, LocalDateTime.of(2024, 9, 10, 10, 0),
                LocalDateTime.of(2024, 10, 10, 10, 0), item, user, BookingStatus.REJECTED);
        List<Booking> bookings = List.of(booking2);
        PageRequest page = PageRequest.of(0, 2);

        when(userRepository.findById(3)).thenReturn(Optional.of(owner));
        when(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(eq(3), any(), any())).thenReturn(bookings);

        List<ResponseBookingDto> result = bookingService.getAllItemOwnerBookings(3, State.REJECTED, 0, 2);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(2, result.get(0).getId());
    }
}
