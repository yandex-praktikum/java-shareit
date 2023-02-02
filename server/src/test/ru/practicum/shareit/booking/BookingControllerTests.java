package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.booking.BookingController;
import ru.practicum.booking.BookingService;
import ru.practicum.booking.Status;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoResponse;
import ru.practicum.exception.ErrorHandler;
import ru.practicum.exception.UnknownStatusException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.shareit.Config.BookingControllerTestConfig;
import ru.practicum.shareit.Config.WebConfig;
import ru.practicum.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({BookingController.class, BookingControllerTestConfig.class, WebConfig.class, Exception.class})
public class BookingControllerTests {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private BookingDtoResponse bookingDtoResponse;
    private BookingDto bookingDto;
    private Item item;
    private User user;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new ErrorHandler(), controller)
                .build();
        item = new Item(1L, "Item1", "description", true, 1L, null);
        user = new User(1L, "User1", "user1@mail.com");
        bookingDto = BookingDto.builder()
                .id(1L)
                .status(Status.WAITING)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(5))
                .bookerId(1L)
                .itemId(1L)
                .build();

        bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .status(Status.WAITING)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(5))
                .booker(user)
                .item(item)
                .build();
        mapper.findAndRegisterModules();
    }

    @Test
    void createBookingTest() throws Exception {
        when(bookingService.create(any(), any()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end", is(notNullValue())));

        when(bookingService.create(any(), any()))
                .thenThrow(ValidationException.class);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingTest() throws Exception {
        bookingDtoResponse.setStatus(Status.APPROVED);
        when(bookingService.update(1L, 1L, true))
                .thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end", is(notNullValue())));
    }

    @Test
    void getBookingByIdtest() throws Exception {
        when(bookingService.getById(any(), any()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end", is(notNullValue())));
    }

    @Test
    void getBookingByOwnerTest() throws Exception {
        when(bookingService.getByOwnerId(any(), any(), any(), any()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$[0].start", is(notNullValue())))
                .andExpect(jsonPath("$[0].end", is(notNullValue())));
    }

    @Test
    void getAllBookingsTest() throws Exception {
        when(bookingService.getByUserId(any(), any(), any(), any()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDtoResponse.getStatus().toString())))
                .andExpect(jsonPath("$[0].start", is(notNullValue())))
                .andExpect(jsonPath("$[0].end", is(notNullValue())));
    }

    @Test
    void getAllBookingsTestException() throws Exception {
        when(bookingService.getByUserId(any(), any(), any(), any()))
                .thenThrow(new UnknownStatusException("BAD"));
        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBookingsTestException2() throws Exception {
        when(bookingService.getByUserId(any(), any(), any(), any()))
                .thenThrow(UserNotFoundException.class);
        mvc.perform(get("/bookings").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound());
    }
}
