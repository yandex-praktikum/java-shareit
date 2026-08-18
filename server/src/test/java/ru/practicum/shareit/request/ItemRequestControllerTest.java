package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void create_shouldReturnCreatedRequest() throws Exception {
        ItemRequestCreateDto requestDto = new ItemRequestCreateDto();
        requestDto.setDescription("Нужна дрель");

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(1L);
        responseDto.setDescription("Нужна дрель");
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(List.of());

        when(itemRequestService.create(1L, requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна дрель"))
                .andExpect(jsonPath("$.items").isArray());

        verify(itemRequestService).create(1L, requestDto);
    }

    @Test
    void getUserRequests_shouldReturnUserRequests() throws Exception {
        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(1L);
        responseDto.setDescription("Нужна дрель");
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(List.of());

        when(itemRequestService.getUserRequests(1L))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description")
                        .value("Нужна дрель"));

        verify(itemRequestService).getUserRequests(1L);
    }

    @Test
    void getAllRequests_shouldReturnOtherUsersRequests() throws Exception {
        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(2L);
        responseDto.setDescription("Нужен шуруповёрт");
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(List.of());

        when(itemRequestService.getAllRequests(1L))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].description")
                        .value("Нужен шуруповёрт"));

        verify(itemRequestService).getAllRequests(1L);
    }

    @Test
    void getRequestById_shouldReturnRequest() throws Exception {
        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(5L);
        responseDto.setDescription("Нужен велосипед");
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(List.of());

        when(itemRequestService.getRequestById(5L))
                .thenReturn(responseDto);

        mockMvc.perform(get("/requests/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.description")
                        .value("Нужен велосипед"));

        verify(itemRequestService).getRequestById(5L);
    }
}