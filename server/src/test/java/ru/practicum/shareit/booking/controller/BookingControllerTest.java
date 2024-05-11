package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void addBooking() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);
        UserDto userDto1 = new UserDto(2, "User2", "user2@mail.ru");
        ResponseBookingDto responseBookingDto = new ResponseBookingDto(1, LocalDateTime.of(2000, 1, 1, 10, 0),
                LocalDateTime.of(2001, 1, 1, 10, 0), itemDto, userDto1, BookingStatus.WAITING);
        BookingDto bookingDto = new BookingDto(1, LocalDateTime.of(2000, 1, 1, 10, 0),
                LocalDateTime.of(2001, 1, 1, 10, 0), 1, 1, BookingStatus.WAITING);

        when(bookingService.addBooking(any(), eq(1))).thenReturn(responseBookingDto);

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", 1)
                .content(objectMapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseBookingDto.getId())))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.booker.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.status", is("WAITING")));

        verify(bookingService, times(1)).addBooking(any(), eq(1));
    }

    @Test
    void updateBooking() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);
        UserDto userDto1 = new UserDto(2, "User2", "user2@mail.ru");
        ResponseBookingDto responseBookingDto = new ResponseBookingDto(1, LocalDateTime.of(2000, 1, 1, 10, 0),
                LocalDateTime.of(2001, 1, 1, 10, 0), itemDto, userDto1, BookingStatus.APPROVED);

        when(bookingService.patchBooking(eq(1), eq(1), eq(true))).thenReturn(responseBookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", responseBookingDto.getId())
                .header("X-Sharer-User-Id", 1)
                .param("approved", "true"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseBookingDto.getId())))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.booker.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.status", is("APPROVED")));

        verify(bookingService, times(1)).patchBooking(eq(1), eq(1), eq(true));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);
        UserDto userDto1 = new UserDto(2, "User2", "user2@mail.ru");
        ResponseBookingDto responseBookingDto = new ResponseBookingDto(1, LocalDateTime.of(2000, 1, 1, 10, 0),
                LocalDateTime.of(2001, 1, 1, 10, 0), itemDto, userDto1, BookingStatus.APPROVED);

        when(bookingService.getBookingById(eq(1), eq(1))).thenReturn(responseBookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", responseBookingDto.getId())
                .header("X-Sharer-User-Id", 1))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(responseBookingDto.getId())))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.booker.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.status", is("APPROVED")));

        verify(bookingService, times(1)).getBookingById(eq(1), eq(1));
    }

    @Test
    void getAllUsersBookingsTest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);
        UserDto userDto1 = new UserDto(2, "User2", "user2@mail.ru");
        ResponseBookingDto responseBookingDto = new ResponseBookingDto(1, LocalDateTime.of(2000, 1, 1, 10, 0),
                LocalDateTime.of(2001, 1, 1, 10, 0), itemDto, userDto1, BookingStatus.APPROVED);

        when(bookingService.getAllUsersBookings(eq(1), any(), eq(0), eq(1))).thenReturn(List.of(responseBookingDto));

        mockMvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", 1)
                .param("from", "0")
                .param("size", "1")
                .param("state", "ALL"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(responseBookingDto.getId())))
                .andExpect(jsonPath("$[0].start", notNullValue()))
                .andExpect(jsonPath("$[0].end", notNullValue()))
                .andExpect(jsonPath("$[0].item.id", is(itemDto.getId())))
                .andExpect(jsonPath("$[0].booker.name", is(userDto1.getName())))
                .andExpect(jsonPath("$[0].status", is("APPROVED")));

        verify(bookingService, times(1)).getAllUsersBookings(eq(1), any(), eq(0), eq(1));
    }

    @Test
    void getAllItemOwnersBookingsTest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);
        UserDto userDto1 = new UserDto(2, "User2", "user2@mail.ru");
        ResponseBookingDto responseBookingDto = new ResponseBookingDto(1, LocalDateTime.of(2000, 1, 1, 10, 0),
                LocalDateTime.of(2001, 1, 1, 10, 0), itemDto, userDto1, BookingStatus.APPROVED);

        when(bookingService.getAllItemOwnerBookings(eq(1), any(), eq(0), eq(1))).thenReturn(List.of(responseBookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "1")
                        .param("state", "ALL"))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(responseBookingDto.getId())))
                .andExpect(jsonPath("$[0].start", notNullValue()))
                .andExpect(jsonPath("$[0].end", notNullValue()))
                .andExpect(jsonPath("$[0].item.id", is(itemDto.getId())))
                .andExpect(jsonPath("$[0].booker.name", is(userDto1.getName())))
                .andExpect(jsonPath("$[0].status", is("APPROVED")));

        verify(bookingService, times(1)).getAllItemOwnerBookings(eq(1), any(), eq(0), eq(1));
    }
}
