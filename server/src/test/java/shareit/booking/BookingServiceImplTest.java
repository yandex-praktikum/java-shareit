package shareit.booking;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceImplTest {
    /*

    @Autowired
    private BookingServiceImpl service;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookingRepository bookingRepository;

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    private final User user1 = User.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("user1")
            .email("email1@ya.ru")
            .build();

    private final Item item1 = Item.builder()
            .id(1L)
            .owner(user1)
            .name("item1")
            .description("desc1")
            .available(true)
            .build();

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .owner(user1.getId())
            .name("item1")
            .description("desc1")
            .available(true)
            .build();

    private final Booking booking1 = Booking.builder()
            .id(1L)
            .item(item1)
            .booker(user1)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now())
            .status(BookingStatus.APPROVED)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .booker(user1.getId())
            .itemId(item1.getId())
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now())
            .status(BookingStatus.APPROVED.toString())
            .build();

    private final BookingResponseDto responseDto = BookingResponseDto.builder()
            .id(1L)
            .item(itemDto)
            .booker(userDto)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now())
            .status(BookingStatus.APPROVED.toString())
            .build();

    @Test
    public void createBooking() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking1);

        BookingDto dto = bookingMapper.mapBookingDto(booking1);
        BookingResponseDto result = service.createBooking(user1.getId(), dto);

        verify(bookingRepository, times(1)).save(any());

        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getBooker(), result.getBooker().getId());
        assertEquals(dto.getStart(), result.getStart());
        assertEquals(dto.getEnd(), result.getEnd());
    }

     */
}