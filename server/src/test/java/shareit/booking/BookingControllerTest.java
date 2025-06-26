package shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

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

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .booker(user1.getId())
            .itemId(item1.getId())
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now())
            .build();

    private final BookingResponseDto responseDto = BookingResponseDto.builder()
            .id(1L)
            .item(itemDto)
            .booker(userDto)
            .start(LocalDateTime.now().minusDays(1))
            .end(LocalDateTime.now())
            .status(BookingStatus.WAITING.toString())
            .build();

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(eq(1L), any(BookingDto.class))).thenReturn(responseDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(bookingService).createBooking(eq(1L), any(BookingDto.class));
    }

    @Test
    void approveBooking() throws Exception {
        when(bookingService.approveBooking(1L, 1L, true)).thenReturn(responseDto);

        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(bookingService).approveBooking(1L, 1L, true);
    }

    @Test
    void canceledBooking() throws Exception {
        when(bookingService.canceledBooking(1L, 1L)).thenReturn(responseDto);

        mvc.perform(patch("/bookings/1/canceled")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(bookingService).canceledBooking(1L, 1L);
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBooking(1L, 1L)).thenReturn(responseDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(bookingService).getBooking(1L, 1L);
    }

    @Test
    void getBookingsByState() throws Exception {
        when(bookingService.getBookingByState(1L, "ALL")).thenReturn(List.of(responseDto));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(bookingService).getBookingByState(1L, "ALL");
    }

    @Test
    void getBookingsAllItemsByState() throws Exception {
        when(bookingService.getBookingsAllItemsByState("ALL", 1L)).thenReturn(List.of(responseDto));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(bookingService).getBookingsAllItemsByState("ALL", 1L);
    }
}
