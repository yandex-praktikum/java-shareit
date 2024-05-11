package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private MockMvc mockMvc;
    private ItemRequestDto itemRequestDto1;
    private ItemRequestDto itemRequestDto2;
    private ItemDtoForRequest itemDtoForRequest1;
    private ItemDtoForRequest itemDtoForRequest2;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        itemDtoForRequest1 = new ItemDtoForRequest(1, "item1", "item1Description", true, 1);
        itemDtoForRequest2 = new ItemDtoForRequest(2, "item2", "item2Description", true, 2);
        itemRequestDto1 = new ItemRequestDto(1, "itemRequest1Description", LocalDateTime.now());
        itemRequestDto2 = new ItemRequestDto(2, "itemRequest2Description", LocalDateTime.now(), List.of(itemDtoForRequest1, itemDtoForRequest2));
    }

    @Test
    void addItemRequest() throws Exception {
        when(itemRequestService.addItemRequest(any(), eq(1))).thenReturn(itemRequestDto1);

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", 1)
                .content(objectMapper.writeValueAsString(itemRequestDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$.items", nullValue()));

        verify(itemRequestService, times(1)).addItemRequest(any(), eq(1));
    }

    @Test
    void getAllRequesterItemRequestTest() throws Exception {
        when(itemRequestService.getAllRequesterItemRequests(eq(1))).thenReturn(List.of(itemRequestDto1, itemRequestDto2));

        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$[0].items", nullValue()))
                .andExpect(jsonPath("$[1].id", is(itemRequestDto2.getId())))
                .andExpect(jsonPath("$[1].description", is(itemRequestDto2.getDescription())))
                .andExpect(jsonPath("$[1].items", hasSize(2)))
                .andExpect(jsonPath("$[1].items[0].id", is(itemDtoForRequest1.getId())))
                .andExpect(jsonPath("$[1].items[0].name", is(itemDtoForRequest1.getName())))
                .andExpect(jsonPath("$[1].items[0].description", is(itemDtoForRequest1.getDescription())))
                .andExpect(jsonPath("$[1].items[0].available", is(true)))
                .andExpect(jsonPath("$[1].items[0].requestId", is(1)))
                .andExpect(jsonPath("$[1].items[1].id", is(itemDtoForRequest2.getId())))
                .andExpect(jsonPath("$[1].items[1].name", is(itemDtoForRequest2.getName())))
                .andExpect(jsonPath("$[1].items[1].description", is(itemDtoForRequest2.getDescription())))
                .andExpect(jsonPath("$[1].items[1].available", is(true)))
                .andExpect(jsonPath("$[1].items[1].requestId", is(2)));

        verify(itemRequestService, times(1)).getAllRequesterItemRequests(eq(1));
    }

    @Test
    void getItemRequestByIdTest() throws Exception {
        when(itemRequestService.getItemRequestById(eq(1), eq(2))).thenReturn(itemRequestDto2);

        mockMvc.perform(get("/requests/{requestId}", itemRequestDto2.getId())
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto2.getId())))
                .andExpect(jsonPath("$.description", is(itemRequestDto2.getDescription())))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[0].id", is(itemDtoForRequest1.getId())))
                .andExpect(jsonPath("$.items[0].name", is(itemDtoForRequest1.getName())))
                .andExpect(jsonPath("$.items[0].description", is(itemDtoForRequest1.getDescription())))
                .andExpect(jsonPath("$.items[0].available", is(true)))
                .andExpect(jsonPath("$.items[0].requestId", is(1)))
                .andExpect(jsonPath("$.items[1].id", is(itemDtoForRequest2.getId())))
                .andExpect(jsonPath("$.items[1].name", is(itemDtoForRequest2.getName())))
                .andExpect(jsonPath("$.items[1].description", is(itemDtoForRequest2.getDescription())))
                .andExpect(jsonPath("$.items[1].available", is(true)))
                .andExpect(jsonPath("$.items[1].requestId", is(2)));

        verify(itemRequestService, times(1)).getItemRequestById(eq(1), eq(2));
    }

    @Test
    void getAllRequesterItemRequestsPagination() throws Exception {
        when(itemRequestService.getAllRequesterItemRequestsPagination(eq(1), eq(0), eq(2))).thenReturn(List.of(itemRequestDto1, itemRequestDto2));

        mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "2")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId())))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$[0].items", nullValue()))
                .andExpect(jsonPath("$[1].id", is(itemRequestDto2.getId())))
                .andExpect(jsonPath("$[1].description", is(itemRequestDto2.getDescription())))
                .andExpect(jsonPath("$[1].items", hasSize(2)))
                .andExpect(jsonPath("$[1].items[0].id", is(itemDtoForRequest1.getId())))
                .andExpect(jsonPath("$[1].items[0].name", is(itemDtoForRequest1.getName())))
                .andExpect(jsonPath("$[1].items[0].description", is(itemDtoForRequest1.getDescription())))
                .andExpect(jsonPath("$[1].items[0].available", is(true)))
                .andExpect(jsonPath("$[1].items[0].requestId", is(1)))
                .andExpect(jsonPath("$[1].items[1].id", is(itemDtoForRequest2.getId())))
                .andExpect(jsonPath("$[1].items[1].name", is(itemDtoForRequest2.getName())))
                .andExpect(jsonPath("$[1].items[1].description", is(itemDtoForRequest2.getDescription())))
                .andExpect(jsonPath("$[1].items[1].available", is(true)))
                .andExpect(jsonPath("$[1].items[1].requestId", is(2)));

        verify(itemRequestService, times(1)).getAllRequesterItemRequestsPagination(eq(1), eq(0), eq(2));
    }
}