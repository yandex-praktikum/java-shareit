package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingMapperTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BookingMapper mapper;
    BookingDto dto = new BookingDto();
    Booking booking = new Booking();
    BookingCreateDto createDto = new BookingCreateDto();
    Item item = new Item();
    User user = new User();

    @BeforeEach
    void setModel() {
        item.setId(1L);
        user.setId(1L);

        dto.setBookerId(1L);
        dto.setId(1L);
        dto.setStart(LocalDateTime.of(2023, 1, 1, 12, 10));
        dto.setItemId(1L);
        dto.setEnd(LocalDateTime.of(2023, 1, 1, 12, 15));
        dto.setStatus(BookingStatus.WAITING);

        booking.setBooker(user);
        booking.setItem(item);
        booking.setStart(LocalDateTime.of(2023, 1, 1, 12, 10));
        booking.setEnd(LocalDateTime.of(2023, 1, 1, 12, 15));
        booking.setStatus(BookingStatus.WAITING);
        booking.setCreated(LocalDateTime.of(2022, 1, 1, 12, 15));

        createDto.setCreated(LocalDateTime.of(2022, 1, 1, 12, 15));
        createDto.setItemId(1L);
        createDto.setStatus(BookingStatus.WAITING);
        createDto.setStart(LocalDateTime.of(2023, 1, 1, 12, 10));
        createDto.setEnd(LocalDateTime.of(2023, 1, 1, 12, 15));
        createDto.setBookerId(1L);
    }

    @Test
    void getBookingDtoFromBookingTest() {
        BookingDto dto1 = mapper.toBookingDto(booking);

        assertEquals(dto1.getId(), booking.getId());
        assertEquals(dto1.getBookerId(), booking.getBooker().getId());
        assertEquals(dto1.getItemId(), booking.getItem().getId());
        assertEquals(dto1.getStatus(), booking.getStatus());
        assertEquals(dto1.getStart(), booking.getStart());
        assertEquals(dto1.getEnd(), booking.getEnd());
    }

    @Test
    void toBookingFromCreatedDtoTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Booking booking1 = mapper.toBookingFromCreatedDto(createDto, 1L);

        assertEquals(createDto.getBookerId(), booking1.getBooker().getId());
        assertEquals(createDto.getItemId(), booking1.getItem().getId());
        assertEquals(createDto.getStatus(), booking1.getStatus());
        assertEquals(createDto.getStart(), booking1.getStart());
        assertEquals(createDto.getEnd(), booking1.getEnd());
    }

    @Test
    void toBookingUpdDtoFromBookingTest() {
        when(userMapper.toUserDto(any())).thenReturn(new UserDto(1L, "name", "e@e.ru"));

        BookingUpdDto updDto = mapper.toBookingUpdDto(booking);

        assertEquals(updDto.getId(), booking.getId());
        assertNotNull(createDto.getBookerId());
        assertNotNull(createDto.getItemId());
        assertEquals(createDto.getStatus(), booking.getStatus());
        assertEquals(createDto.getStart(), booking.getStart());
        assertEquals(createDto.getEnd(), booking.getEnd());
    }

    @Test
    void toBookingUpdDtoListFromBookingListTest() {
        List<BookingUpdDto> list = mapper.toBookingUpdDtoList(List.of(booking));

        assertNotNull(list);
        assertEquals(list.size(), 1);
    }
}
