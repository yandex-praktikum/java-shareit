package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.booking.*;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.exception.*;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.ItemService;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;
import ru.practicum.user.UserService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.utilities.PaginationConverter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingUnitTests {
    private BookingService bookingService;
    private BookingDto bookingDto;
    private Booking booking;
    private ItemDto itemDto;
    private ItemDtoResponse itemDtoResponse;
    private User user;
    private Item item;
    private Pageable pageable;
    private UserDto userDto;
    private BookingDtoResponse bookingDtoResponse;


    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository,
                userRepository, new PaginationConverter());

        bookingDto = BookingDto.builder()
                .id(1L)
                .status(Status.WAITING)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(5))
                .bookerId(1L)
                .itemId(1L)
                .build();
        item = new Item(1L, "Item1", "description", true, 1L, null);
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(1L)
                .build();

        user = new User(1L, "User1", "user1@mail.com");
        userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();


        booking = new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(),
                item, user, bookingDto.getStatus());

        user = new User(1L, "User1", "user1@mail.com");

        item = new Item(1L, "Item1", "description", true, 1L, null);
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(1L)
                .build();
        itemDtoResponse = ItemDtoResponse.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .userId(1L)
                .build();

        bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .status(Status.WAITING)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(5))
                .booker(user)
                .item(item)
                .build();
        pageable = PageRequest.of(0, 1);
    }

    @Test
    void createBookingTest() {


        assertThrows(UserNotFoundException.class, () -> bookingService.create(1L, bookingDto));
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        assertThrows(ItemNotFoundException.class, () -> bookingService.create(1L, bookingDto));

        Mockito
                .when(itemRepository.findItemById(anyLong()))
                .thenReturn(item);

        bookingDto.setEnd(LocalDateTime.now().minusMonths(1));

        assertThrows(OwnerCreateBookingException.class, () -> bookingService.create(1L, bookingDto));

        item.setOwnerId(4L);

        assertThrows(ValidationException.class, () -> bookingService.create(1L, bookingDto));

        bookingDto.setEnd(LocalDateTime.now().plusHours(5));

        item.setAvailable(false);

        assertThrows(ItemNotAvailableException.class, () -> bookingService.create(1L, bookingDto));

        booking = Booking.builder()
                .id(2L)
                .build();
        assertThat(booking.getId() == 2L);
        booking.setId(1L);

        assertThat(booking.getItem() == item);
        assertThat(booking.getBooker() == user);
    }

    @Test
    void createVerifyTest() {
        Mockito
                .when(itemRepository.findItemById(anyLong()))
                .thenReturn(item);

        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        item.setOwnerId(4L);

        bookingService.create(1L, bookingDto);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(any());
    }

    @Test
    void updateBookingTest() {
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> bookingService.update(1L, 1L,
                true));

        booking.setStatus(Status.APPROVED);

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        assertThrows(BookingNotFoundException.class, () -> bookingService.update(1L, 1L,
                true));
        Mockito
                .when(bookingRepository.findBooking(any()))
                .thenReturn(booking);

        assertThrows(BadUserForUpdateException.class, () -> bookingService.update(1L, 1L,
                true));
        user.setId(33L);
        item.setOwnerId(33L);
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);
        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(booking);

        user.setId(1L);
        item.setOwnerId(user.getId());

        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);


        assertThrows(ValidationException.class, () -> bookingService.update(1L, 1L,
                true));

        booking.setStatus(Status.WAITING);
        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(booking);
        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(booking);

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        Mockito
                .when(bookingRepository.findBooking(any()))
                .thenReturn(booking);

        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);


        assertThat(bookingService.update(1L, 1L, true).getStatus().equals(Status.APPROVED));
        assertThat(bookingService.update(1L, 1L, false).getStatus().equals(Status.REJECTED));


        item.setOwnerId(33L);

        Mockito
                .when(itemRepository.findItemById(any()))
                .thenReturn(item);

        assertThrows(NoRightsToUpdateException.class, () -> bookingService.update(1L, 1L,
                true));
    }

    @Test
    void getBookingByIdTest() {
        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(booking);

        assertNotNull(bookingService.getById(1L, 1L));

        Mockito
                .when(bookingRepository.findBookingById(any(), any()))
                .thenReturn(null);
        assertThrows(BookingNotFoundException.class, () -> bookingService.getById(1L, 1L));
    }

    @Test
    void getBookingsByUserTest() {
        assertThrows(UserNotFoundException.class, () ->
                bookingService.getByUserId(1L, "Unsupported", null, null));
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        assertThrows(UnknownStatusException.class, () ->
                bookingService.getByUserId(1L, "Unsupported", null, null));

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        Mockito
                .when(bookingRepository.findBookingsByBooker(any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByBookerFuture(any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByBookerCurrent(any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByBookerPast(any(), any()))
                .thenReturn(List.of(booking));

        assertThat(bookingService.getByUserId(1L, "ALL", null, null).size() == 1);
        assertThat(bookingService.getByUserId(1L, "FUTURE", null, null).size() == 1);
        assertThat(bookingService.getByUserId(1L, "CURRENT", null, null).size() == 1);
        assertThat(bookingService.getByUserId(1L, "PAST", null, null).size() == 1);
        assertThat(bookingService.getByUserId(1L, "WAITING", null, null).size() == 1);
        assertThat(bookingService.getByUserId(1L, "REJECTED", null, null).size() == 1);
    }

    @Test
    void getBookingsByOwnerTest() {

        assertThrows(UserNotFoundException.class, () ->
                bookingService.getByOwnerId(1L, "Unsupported", null, null));
        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        assertThrows(UnknownStatusException.class, () ->
                bookingService.getByOwnerId(1L, "Unsupported", null, null));

        Mockito
                .when(userRepository.getUserById(any()))
                .thenReturn(user);

        Mockito
                .when(bookingRepository.findBookingsByOwner(any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByOwnerFuture(any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByOwnerCurrent(any(), any()))
                .thenReturn(List.of(booking));
        Mockito
                .when(bookingRepository.findBookingsByOwnerPast(any(), any()))
                .thenReturn(List.of(booking));

        assertThat(bookingService.getByOwnerId(1L, "ALL", null, null).size() == 1);
        assertThat(bookingService.getByOwnerId(1L, "FUTURE", null, null).size() == 1);
        assertThat(bookingService.getByOwnerId(1L, "CURRENT", null, null).size() == 1);
        assertThat(bookingService.getByOwnerId(1L, "PAST", null, null).size() == 1);
        assertThat(bookingService.getByOwnerId(1L, "WAITING", null, null).size() == 1);
        assertThat(bookingService.getByOwnerId(1L, "REJECTED", null, null).size() == 1);
    }
}
