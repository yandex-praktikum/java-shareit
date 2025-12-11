package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        owner = new User(1L, "Owner", "owner@mail.com");
        booker = new User(2L, "Booker", "booker@mail.com");
        item = new Item(1L, "Дрель", "Мощная дрель", true, owner.getId(), null);

        bookingDto = new BookingDto(null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                item.getId(),
                booker.getId(),
                BookingStatus.WAITING);

        booking = BookingMapper.toBooking(bookingDto);
        booking.setId(1L);
    }

    @Test
    void createBooking_whenValidData_thenBookingCreated() {
        when(userRepository.existsById(booker.getId())).thenReturn(true);
        when(itemService.getItemById(item.getId(), booker.getId()))
                .thenReturn(new ItemDto(item.getId(), item.getName(), item.getDescription(),
                        item.getAvailable(), item.getRequestId()));
        when(itemService.getItemsByOwner(booker.getId()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDto result = bookingService.createBooking(bookingDto, booker.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_whenItemNotAvailable_thenThrowException() {
        when(userRepository.existsById(booker.getId())).thenReturn(true);
        when(itemService.getItemById(item.getId(), booker.getId()))
                .thenReturn(new ItemDto(item.getId(), item.getName(), item.getDescription(),
                        false, item.getRequestId()));

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(bookingDto, booker.getId()));
    }

    @Test
    void createBooking_whenBookOwnItem_thenThrowException() {
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemService.getItemById(item.getId(), owner.getId()))
                .thenReturn(new ItemDto(item.getId(), item.getName(), item.getDescription(),
                        true, item.getRequestId()));
        when(itemService.getItemsByOwner(owner.getId()))
                .thenReturn(List.of(new ItemDto(item.getId(), item.getName(),
                        item.getDescription(), true, item.getRequestId())));

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.createBooking(bookingDto, owner.getId()));
    }

    @Test
    void createBooking_whenEndBeforeStart_thenThrowException() {
        bookingDto.setStart(LocalDateTime.now().plusDays(2));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        when(userRepository.existsById(booker.getId())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> bookingService.createBooking(bookingDto, booker.getId()));
    }

    @Test
    void approveBooking_whenOwnerApproves_thenBookingApproved() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.approveBooking(booking.getId(), owner.getId(), true);

        assertNotNull(result);
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void approveBooking_whenNotOwner_thenThrowException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(AccessDeniedException.class,
                () -> bookingService.approveBooking(booking.getId(), 999L, true));
    }

    @Test
    void approveBooking_whenAlreadyProcessed_thenThrowException() {
        booking.setStatus(BookingStatus.APPROVED);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(booking.getId(), owner.getId(), true));
    }

    @Test
    void getBookingById_whenOwnerRequests_thenReturnBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemService.getItemsByOwner(owner.getId()))
                .thenReturn(List.of(new ItemDto(item.getId(), item.getName(),
                        item.getDescription(), true, item.getRequestId())));
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDto result = bookingService.getBookingById(booking.getId(), owner.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingById_whenBookerRequests_thenReturnBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemService.getItemsByOwner(booker.getId())).thenReturn(Collections.emptyList());
        when(userRepository.findById(booker.getId())).thenReturn(Optional.of(booker));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        BookingDto result = bookingService.getBookingById(booking.getId(), booker.getId());

        assertNotNull(result);
        assertEquals(booking.getId(), result.getId());
    }

    @Test
    void getBookingById_whenUnauthorizedUser_thenThrowException() {
        User unauthorizedUser = new User(3L, "Stranger", "stranger@mail.com");

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(itemService.getItemsByOwner(unauthorizedUser.getId()))
                .thenReturn(Collections.emptyList());

        assertThrows(AccessDeniedException.class,
                () -> bookingService.getBookingById(booking.getId(), unauthorizedUser.getId()));
    }

    @Test
    void cancelBooking_whenBookerCancels_thenBookingCanceled() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.cancelBooking(booking.getId(), booker.getId());

        assertNotNull(result);
        assertEquals(BookingStatus.CANCELED, result.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void cancelBooking_whenNotBooker_thenThrowException() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThrows(EntityNotFoundException.class,
                () -> bookingService.cancelBooking(booking.getId(), 999L));
    }

    @Test
    void getBookingsByBooker_whenAllState_thenReturnAllBookings() {
        when(userRepository.existsById(booker.getId())).thenReturn(true);
        when(bookingRepository.findByBookerId(booker.getId()))
                .thenReturn(List.of(booking));
        when(userRepository.findAllById(any())).thenReturn(List.of(booker));
        when(itemRepository.findAllById(any())).thenReturn(List.of(item));

        List<BookingDto> result = bookingService.getBookingsByBooker(booker.getId(), "ALL");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getBookingsByOwner_whenAllState_thenReturnAllBookings() {
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(itemService.getItemsByOwner(owner.getId()))
                .thenReturn(List.of(new ItemDto(item.getId(), item.getName(),
                        item.getDescription(), true, item.getRequestId())));
        when(bookingRepository.findByItemIdIn(any()))
                .thenReturn(List.of(booking));
        when(userRepository.findAllById(any())).thenReturn(List.of(booker));
        when(itemRepository.findAllById(any())).thenReturn(List.of(item));

        List<BookingDto> result = bookingService.getBookingsByOwner(owner.getId(), "ALL");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
