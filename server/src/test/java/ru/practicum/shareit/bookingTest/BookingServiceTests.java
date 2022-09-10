package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.dto.BookingUpdDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exceptions.BookingNotFoundException;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.exceptions.NotAvailableException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {
    Booking booking;
    User user;
    Item item;
    @Mock
    private BookingRepository repository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private BookingServiceImpl service;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void registerModule() {
        mapper.registerModule(new JavaTimeModule());
        booking = new Booking();
        user = new User();
        item = new Item();

        item.setId(1L);
        item.setOwnerId(1L);
        item.setAvailable(true);
        user.setId(1L);
        booking.setBooker(user);
        booking.setItem(item);
    }

    UserDto userDto = new UserDto(1L, "я", "iam@mail.ru");
    ItemDto itemDto = new ItemDto(1L, "молоток", "ручной", true, null);
    BookingUpdDto dto = new BookingUpdDto(1L,
            LocalDateTime.of(2023, 1, 1, 12, 10),
            LocalDateTime.of(2023, 1, 1, 12, 15),
            BookingStatus.WAITING, itemDto, userDto);

    @Test
    void whenTryCreateBookingThenReturnBookingNotNull() {
        item.setOwnerId(2L);

        when(bookingMapper.toBookingFromCreatedDto(any(), anyLong())).thenReturn(booking);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(repository.save(any())).thenReturn(booking);
        when(bookingMapper.toBookingUpdDto(any())).thenReturn(dto);

        BookingUpdDto save = service.create(any(), anyLong());

        assertNotNull(save);
        assertEquals(save.getId(), 1L);
    }

    @Test
    void whenTryCreateBookingForYourItemThenThrowItemNotFoundException() {
        when(bookingMapper.toBookingFromCreatedDto(any(), anyLong())).thenReturn(booking);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(ItemNotFoundException.class, () -> service.create(any(), anyLong()));
    }

    @Test
    void whenTryCreateBookingNotAvailableItemThenThrowNotAvailableException() {
        item.setAvailable(false);

        when(bookingMapper.toBookingFromCreatedDto(any(), anyLong())).thenReturn(booking);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotAvailableException.class, () -> service.create(any(), anyLong()));
    }

    @Test
    void whenTryUpdateBookingThenReturnBookingNotNull() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(repository.save(any())).thenReturn(booking);
        when(bookingMapper.toBookingUpdDto(any())).thenReturn(dto);

        BookingUpdDto upd = service.update(true, 1L, 1L);

        assertNotNull(upd);
        assertThrows(NotAvailableException.class, () -> service.update(false, 1L, 1L));
    }

    @Test
    void shouldBookingNotFoundExceptionWhenTryUpdateWithBookingNotFound() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> service.update(true, 1L, 1L));
    }

    @Test
    void shouldBookingNotFoundExceptionWhenTryUpdateAndUserNotOwnerItem() {
        item.setOwnerId(2L);

        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        assertThrows(BookingNotFoundException.class, () -> service.update(true, 1L, 1L));
    }

    @Test
    void whenTryGetBookingThenReturnBookingNotNull() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingUpdDto(any())).thenReturn(dto);

        assertNotNull(service.getBookingById(1L, 1L));
    }

    @Test
    void whenTryGetNotExistentBookingThenReturnException() {
        assertThrows(BookingNotFoundException.class, () -> service.getBookingById(1L, 1L));
    }

    @Test
    void shouldExceptionWhenGetBookingAndUserNotOwnerItemAndNotBooker() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(BookingNotFoundException.class, () -> service.getBookingById(1L, 3L));
    }

    @Test
    void shouldListSize1WhenTryGetAll() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by("start"));
        when(userService.getUserById(anyLong())).thenReturn(new User());
        when(repository.findByBookerIdOrderByStartDesc(1L, pageable)).thenReturn(List.of(new Booking()));
        when(bookingMapper.toBookingUpdDtoList(anyList())).thenReturn(List.of(dto));
        when(repository.findByBookerIdAndStatusOrderByStartDesc(1L,
                BookingStatus.WAITING,
                pageable)).thenReturn(List.of(new Booking()));

        List<BookingUpdDto> list = service.getAllByBookerId(StateEnum.ALL, 1L, 1, 1);
        List<BookingUpdDto> list1 = service.getAllByBookerId(StateEnum.WAITING, 1L, 1, 1);
        assertEquals(list.size(), 1);
        assertEquals(list1.size(), 1);
    }

    @Test
    void shouldListSize1WhenTryGetAllWhereOwnerOfItemsTest() {
        Pageable pageable = PageRequest.of(1, 1, Sort.by("start"));

        when(itemRepository.getAllByOwnerId(anyLong())).thenReturn(List.of(new Item()));
        when(userService.getUserById(anyLong())).thenReturn(new User());
        when(repository.getBookingsByItemOwnerIdOrderByStartDesc(1L, pageable)).thenReturn(List.of(new Booking()));
        when(bookingMapper.toBookingUpdDtoList(anyList())).thenReturn(List.of(dto));
        when(repository.findByItemOwnerIdAndStatusOrderByStartDesc(1L,
                BookingStatus.WAITING,
                pageable)).thenReturn(List.of(new Booking()));

        List<BookingUpdDto> list = service.getAllWhereOwnerOfItems(StateEnum.ALL, 1L, 1, 1);
        List<BookingUpdDto> list1 = service.getAllWhereOwnerOfItems(StateEnum.WAITING, 1L, 1, 1);
        assertEquals(list.size(), 1);
        assertEquals(list1.size(), 1);
    }

    @Test
    void verifyUse1TimesDeleteMethod() {
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}

