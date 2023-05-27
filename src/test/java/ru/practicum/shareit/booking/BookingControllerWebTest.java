package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ErrorHandler;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.IncorrectBookingParameterException;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectParameterException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utilits.Variables;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@ExtendWith(MockitoExtension.class)
public class BookingControllerWebTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Autowired
    private MockMvc mockMvc;

    private BookingDto bookingDto;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    private void setUp() {
        mockMvc = standaloneSetup(bookingController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        Date dateBeg = getDate("2023-03-29");
        Date dateEnd = getDate("2023-04-15");

        User owner = new User(1, "eee@email.ru", "Eva");
        Item item = new Item(1, "carpet", "description", true, null, owner);
        ItemDto itemDto = ItemMapper.toItemDto(item);

        bookingDto = new BookingDto(1, dateBeg, dateEnd, 1, itemDto, null, 2, null);
    }

    @Test
    public void shouldNotGetBookingsWithErrorBookingStatus() throws Exception {
        mockMvc.perform(get("/bookings")
                .header(Variables.USER_ID, 2)
                .param("state", "NOSTATE")
                .param("from", "1")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotGetBookingsWithNotFoundUser() throws Exception {
        Mockito.when(
                bookingService.getBooking(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())
        ).thenThrow(new UserNotFoundException("Пользователь не найден"));

        mockMvc.perform(
                get("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Variables.USER_ID, 99)
                        .param("state", BookingStatus.WAITING.label)
                        .param("from", "1")
                        .param("size", "10"))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertEquals("Пользователь не найден", result.getResolvedException().getMessage()));
    }

    @Test
    public void shouldNotGetNotExistsBooking() throws Exception {
        Mockito.when(
                bookingService.getBooking(Mockito.anyInt(), Mockito.anyInt())
        ).thenThrow(new BookingNotFoundException("Неверные параметры"));

        mockMvc.perform(
                get("/bookings/{bookingId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Variables.USER_ID, 99))
                .andExpect(status().isNotFound())
                .andExpect(result -> Assertions.assertEquals("Неверные параметры", result.getResolvedException().getMessage()));
    }


    @Test
    public void shouldSuccessGetBookingById() throws Exception {
        Mockito.when(bookingService.getBooking(Mockito.anyInt(), any())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Variables.USER_ID, 2)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSuccessGetBooking() throws Exception {
        Mockito.when(bookingService.getBooking(Mockito.any(), any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .param("state", BookingStatus.WAITING.label)
                .param("from", "1")
                .param("size", "10")
                .header(Variables.USER_ID, 2)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSuccessGetOwnerBookedItemList() throws Exception {
        Mockito.when(bookingService.ownerItemsBookingLists(Mockito.any(), any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                .contentType(MediaType.APPLICATION_JSON)
                .param("state", BookingStatus.WAITING.label)
                .param("from", "1")
                .param("size", "10")
                .header(Variables.USER_ID, 2)
        )
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSuccessAddBooking() throws Exception {
        Mockito.when(bookingService.booking(Mockito.anyInt(), any()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                .header(Variables.USER_ID, 2)
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId())))
                .andExpect(jsonPath("$.bookerId", is(bookingDto.getBookerId())));
    }

    @Test
    public void shouldReturnErrorMsg() throws Exception {
        Mockito.when(bookingService.booking(Mockito.anyInt(), any())).thenThrow(IncorrectParameterException.class);

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", 2)
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void shouldApprove() throws Exception {
        Mockito.when(bookingService.aprove(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", "1")
                .header(Variables.USER_ID, 2)
                .param("approved", "true")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId())))
                .andExpect(jsonPath("$.bookerId", is(bookingDto.getBookerId())));
    }

    @Test
    public void shouldFailOnApproveWithErrorParam() throws Exception {
        Mockito.when(
                bookingService.aprove(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean())
        )
                .thenThrow(new IncorrectBookingParameterException("Неверные параметры"));

        mockMvc.perform(patch("/bookings/{bookingId}", "1")
                .header(Variables.USER_ID, 2)
                .param("approved", "true")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }

    private Date getDate(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
