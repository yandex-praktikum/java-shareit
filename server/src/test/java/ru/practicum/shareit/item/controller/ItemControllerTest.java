package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void addItemTest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);

        when(itemService.addItem(any(), eq(1))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", 1)
                .content(objectMapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())));

        verify(itemService, times(1)).addItem(any(), eq(1));
    }

    @Test
    void patchItemTest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);

        when(itemService.updateItem(any(), eq(1), eq(1))).thenReturn(itemDto);

        mockMvc.perform(patch("/items/{itemId}", itemDto.getId())
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId())));

        verify(itemService, times(1)).updateItem(any(), eq(1), eq(1));
    }

    @Test
    void getItemByIdTest() throws Exception {
        ShortBookingDto lastBooking = new ShortBookingDto(1,1);
        ShortBookingDto nextBooking = new ShortBookingDto(2, 2);
        ItemDtoBookingsAndComments itemDtoBookingsAndComments = new ItemDtoBookingsAndComments(1, "item", "description", true, lastBooking, nextBooking, new ArrayList<>());

        when(itemService.getItemById(eq(1), eq(1))).thenReturn(itemDtoBookingsAndComments);

        mockMvc.perform(get("/items/{itemId}", itemDtoBookingsAndComments.getId())
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoBookingsAndComments.getId())))
                .andExpect(jsonPath("$.name", is(itemDtoBookingsAndComments.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoBookingsAndComments.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoBookingsAndComments.getAvailable())))
                .andExpect(jsonPath("$.lastBooking.id", is(lastBooking.getId())))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(lastBooking.getBookerId())))
                .andExpect(jsonPath("$.nextBooking.id", is(nextBooking.getId())))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(nextBooking.getBookerId())))
                .andExpect(jsonPath("$.comments", is(itemDtoBookingsAndComments.getComments())));

        verify(itemService, times(1)).getItemById(eq(1), eq(1));
    }

    @Test
    void getUserItemsTest() throws Exception {
        ShortBookingDto lastBooking1 = new ShortBookingDto(1,1);
        ShortBookingDto nextBooking1 = new ShortBookingDto(2, 2);
        ShortBookingDto lastBooking2 = new ShortBookingDto(3,1);
        ShortBookingDto nextBooking2 = new ShortBookingDto(4, 2);
        ItemDtoBookingsAndComments itemDtoBookingsAndComments1 = new ItemDtoBookingsAndComments(1, "item1", "description1", true, lastBooking1, nextBooking1, new ArrayList<>());
        ItemDtoBookingsAndComments itemDtoBookingsAndComments2 = new ItemDtoBookingsAndComments(2, "item2", "description2", false, lastBooking2, nextBooking2, new ArrayList<>());

        when(itemService.getUserItems(eq(1), eq(0), eq(2))).thenReturn(List.of(itemDtoBookingsAndComments1, itemDtoBookingsAndComments2));

        mockMvc.perform(get("/items")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(itemDtoBookingsAndComments1.getId())))
                .andExpect(jsonPath("$[0].name", is(itemDtoBookingsAndComments1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoBookingsAndComments1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoBookingsAndComments1.getAvailable())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(lastBooking1.getId())))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(lastBooking1.getBookerId())))
                .andExpect(jsonPath("$[0].nextBooking.id", is(nextBooking1.getId())))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(nextBooking1.getBookerId())))
                .andExpect(jsonPath("$[0].comments", is(itemDtoBookingsAndComments1.getComments())))
                .andExpect(jsonPath("$[1].id", is(itemDtoBookingsAndComments2.getId())))
                .andExpect(jsonPath("$[1].name", is(itemDtoBookingsAndComments2.getName())))
                .andExpect(jsonPath("$[1].description", is(itemDtoBookingsAndComments2.getDescription())))
                .andExpect(jsonPath("$[1].available", is(itemDtoBookingsAndComments2.getAvailable())))
                .andExpect(jsonPath("$[1].lastBooking.id", is(lastBooking2.getId())))
                .andExpect(jsonPath("$[1].lastBooking.bookerId", is(lastBooking2.getBookerId())))
                .andExpect(jsonPath("$[1].nextBooking.id", is(nextBooking2.getId())))
                .andExpect(jsonPath("$[1].nextBooking.bookerId", is(nextBooking2.getBookerId())))
                .andExpect(jsonPath("$[1].comments", is(itemDtoBookingsAndComments2.getComments())));

        verify(itemService, times(1)).getUserItems(eq(1), eq(0), eq(2));
    }

    @Test
    void getSearchTest() throws Exception {
        ItemDto itemDto = new ItemDto(1, "item", "description", true, 1, 1);

        when(itemService.getSearch(eq("description"), eq(0), eq(2))).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                .param("text", "description")
                .param("from", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId())))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].ownerId", is(itemDto.getOwnerId())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getOwnerId())));

        verify(itemService, times(1)).getSearch(eq("description"), eq(0), eq(2));
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto commentDto = new CommentDto(1, "text", "John", LocalDateTime.now());

        when(itemService.addComment(any(), eq(1), eq(1))).thenReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1)
                .header("X-Sharer-User-Id", 1)
                .content(objectMapper.writeValueAsString(commentDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId())))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", notNullValue()));

        verify(itemService, times(1)).addComment(any(), eq(1), eq(1));
    }
}
