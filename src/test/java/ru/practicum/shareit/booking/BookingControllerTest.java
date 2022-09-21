package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.BookingTestData.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingServiceImpl bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void testCreate() throws Exception {
        when(bookingService.create(anyLong(), anyLong(), any()))
                .thenReturn(bookingDtoToUserCreated);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(createdDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoToUserCreated)));
    }

    @Test
    void testApproveStatus() throws Exception {
        when(bookingService.approveStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoToUser2);

        mvc.perform(patch("/bookings/2?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoToUser2)));
    }

    @Test
    void testGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingDtoToUser1);

        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoToUser1)));
    }

    @Test
    void testGeBookingCurrentUser() throws Exception {
        when(bookingService.getBookingCurrentUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoState));

        mvc.perform(get("/bookings?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDtoState))));
    }

    @Test
    void testGeBookingCurrentOwner() throws Exception {
        when(bookingService.getBookingCurrentOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoState));

        mvc.perform(get("/bookings/owner?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDtoState))));
    }

    @Test
    void testGeBookingCurrentOwnerWrongState() throws Exception {
        when(bookingService.getBookingCurrentOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoState));

        mvc.perform(get("/bookings/owner?state=text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}