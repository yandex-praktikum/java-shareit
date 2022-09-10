package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingUpdDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class BookingControllerTests {
    @Mock
    private BookingService service;
    @InjectMocks
    private BookingController controller;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    UserDto user = new UserDto(1L, "я", "iam@mail.ru");
    ItemDto item = new ItemDto(1L, "молоток", "ручной", true, null);
    BookingUpdDto dto = new BookingUpdDto(1L,
            LocalDateTime.of(2023, 1, 1, 12, 10),
            LocalDateTime.of(2023, 1, 1, 12, 15),
            BookingStatus.WAITING, item, user);

    BookingCreateDto createDto = new BookingCreateDto(LocalDateTime.of(2023, 1, 1, 12, 10),
            LocalDateTime.of(2023, 1, 1, 12, 15), 1L);


    @Test
    void createTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(service.create(any(), anyLong())).thenReturn(dto);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(createDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(service.update(anyBoolean(), anyLong(), anyLong())).thenReturn(dto);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(createDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("approved", String.valueOf(true)))
                .andExpect(status().isOk());
    }

    @Test
    void getTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(service.getBookingById(anyLong(), anyLong())).thenReturn(dto);

        mvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(service.getAllByBookerId(any(), anyLong(), anyInt(), anyInt())).thenReturn((List.of(dto)));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("size", "1")
                        .param("from", "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllWhereOwnerOfItemsTest() throws Exception {
        mapper.registerModule(new JavaTimeModule());
        when(service.getAllWhereOwnerOfItems(any(), anyLong(), anyInt(), anyInt())).thenReturn((List.of(dto)));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("size", "1")
                        .param("from", "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest() throws Exception {

        mvc.perform(delete("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).delete(1L);
    }
}

