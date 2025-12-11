package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ru.practicum.shareit.booking.BookingService bookingService;

    @Test
    void createBooking_whenValidData_thenReturnOk() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        String jsonRequest = objectMapper.writeValueAsString(bookingDto);

        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);
        responseDto.setStart(bookingDto.getStart());
        responseDto.setEnd(bookingDto.getEnd());
        responseDto.setItemId(1L);
        responseDto.setBookerId(1L);
        responseDto.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingService.createBooking(Mockito.any(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void createBooking_whenMissingItemId_thenReturnOk() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        String jsonRequest = objectMapper.writeValueAsString(bookingDto);

        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);
        responseDto.setStart(bookingDto.getStart());
        responseDto.setEnd(bookingDto.getEnd());
        responseDto.setItemId(null);
        responseDto.setBookerId(1L);
        responseDto.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingService.createBooking(Mockito.any(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createBooking_whenStartInPast_thenReturnOk() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().minusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        String jsonRequest = objectMapper.writeValueAsString(bookingDto);

        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);
        responseDto.setStart(bookingDto.getStart());
        responseDto.setEnd(bookingDto.getEnd());
        responseDto.setItemId(1L);
        responseDto.setBookerId(1L);
        responseDto.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingService.createBooking(Mockito.any(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createBooking_whenEndBeforeStart_thenReturnOk() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(2));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        String jsonRequest = objectMapper.writeValueAsString(bookingDto);

        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);
        responseDto.setStart(bookingDto.getStart());
        responseDto.setEnd(bookingDto.getEnd());
        responseDto.setItemId(1L);
        responseDto.setBookerId(1L);
        responseDto.setStatus(BookingStatus.WAITING);

        Mockito.when(bookingService.createBooking(Mockito.any(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void approveBooking_whenValidRequest_thenReturnOk() throws Exception {
        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);
        responseDto.setStart(LocalDateTime.now());
        responseDto.setEnd(LocalDateTime.now().plusDays(1));
        responseDto.setItemId(1L);
        responseDto.setBookerId(2L);
        responseDto.setStatus(BookingStatus.APPROVED);

        Mockito.when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void getBookingById_whenValidRequest_thenReturnOk() throws Exception {
        BookingDto responseDto = new BookingDto();
        responseDto.setId(1L);
        responseDto.setStart(LocalDateTime.now());
        responseDto.setEnd(LocalDateTime.now().plusDays(1));
        responseDto.setItemId(1L);
        responseDto.setBookerId(2L);
        responseDto.setStatus(BookingStatus.APPROVED);

        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.itemId").value(1));
    }

    @Test
    void getBookings_whenValidRequest_thenReturnOk() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingDto.setItemId(1L);
        bookingDto.setBookerId(2L);
        bookingDto.setStatus(BookingStatus.APPROVED);

        List<BookingDto> bookings = List.of(bookingDto);

        Mockito.when(bookingService.getBookingsByBooker(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].itemId").value(1));
    }

    @Test
    void getOwnerBookings_whenValidRequest_thenReturnOk() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingDto.setItemId(1L);
        bookingDto.setBookerId(2L);
        bookingDto.setStatus(BookingStatus.APPROVED);

        List<BookingDto> bookings = List.of(bookingDto);

        Mockito.when(bookingService.getBookingsByOwner(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].itemId").value(1));
    }

    @Test
    void createBooking_whenMissingUserIdHeader_thenReturnBadRequest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        String jsonRequest = objectMapper.writeValueAsString(bookingDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approveBooking_whenMissingApprovedParam_thenReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }
}