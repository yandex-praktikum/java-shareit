package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void createRequest_whenValidData_thenReturnOk() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужна дрель для ремонта");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(1L);
        responseDto.setDescription("Нужна дрель для ремонта");
        responseDto.setRequestorId(1L);
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(Collections.emptyList());

        Mockito.when(itemRequestService.createRequest(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна дрель для ремонта"));
    }

    @Test
    void createRequest_whenEmptyDescription_thenReturnOk() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(1L);
        responseDto.setDescription("");
        responseDto.setRequestorId(1L);
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(Collections.emptyList());

        Mockito.when(itemRequestService.createRequest(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())  // 200 OK, а не 400
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(""));
    }

    @Test
    void createRequest_whenDescriptionTooLong_thenReturnOk() throws Exception {
        String longDescription = "a".repeat(1001);
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription(longDescription);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(1L);
        responseDto.setDescription(longDescription);
        responseDto.setRequestorId(1L);
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(Collections.emptyList());

        Mockito.when(itemRequestService.createRequest(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())  // 200 OK, а не 400
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(longDescription));
    }

    @Test
    void createRequest_whenMissingUserIdHeader_thenReturnBadRequest() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Нужна дрель для ремонта");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestsByRequestor_whenValidRequest_thenReturnOk() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Нужна дрель");
        requestDto.setRequestorId(1L);
        requestDto.setCreated(LocalDateTime.now());

        List<ItemRequestDto> requests = List.of(requestDto);

        Mockito.when(itemRequestService.getRequestsByRequestor(Mockito.any()))
                .thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Нужна дрель"));
    }

    @Test
    void getAllRequests_whenValidRequest_thenReturnOk() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Запрос от другого пользователя");
        requestDto.setRequestorId(2L);
        requestDto.setCreated(LocalDateTime.now());

        List<ItemRequestDto> requests = List.of(requestDto);

        Mockito.when(itemRequestService.getAllRequests(
                Mockito.any(),
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Запрос от другого пользователя"));
    }

    @Test
    void getAllRequests_whenInvalidPagination_thenReturnOk() throws Exception {
        Mockito.when(itemRequestService.getAllRequests(
                Mockito.any(),
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "-1")  // Отрицательное значение
                        .param("size", "0"))  // Нулевой размер
                .andExpect(status().isOk())  // 200 OK, а не 400
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllRequests_whenInvalidSize_thenReturnOk() throws Exception {
        Mockito.when(itemRequestService.getAllRequests(
                Mockito.any(),
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getRequestById_whenValidRequest_thenReturnOk() throws Exception {
        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(1L);
        responseDto.setDescription("Нужна дрель для ремонта");
        responseDto.setRequestorId(1L);
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(Collections.emptyList());

        Mockito.when(itemRequestService.getRequestById(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна дрель для ремонта"));
    }

    @Test
    void getAllRequests_whenNoRequests_thenReturnEmptyList() throws Exception {
        Mockito.when(itemRequestService.getAllRequests(
                Mockito.any(),
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getRequestsByRequestor_whenNoRequests_thenReturnEmptyList() throws Exception {
        Mockito.when(itemRequestService.getRequestsByRequestor(Mockito.any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getRequestById_whenNotFound_thenReturnNotFound() throws Exception {
        Mockito.when(itemRequestService.getRequestById(Mockito.any(), Mockito.any()))
                .thenThrow(new ru.practicum.shareit.exception.EntityNotFoundException("Запрос не найден"));

        mockMvc.perform(get("/requests/999")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllRequests_withDefaultPagination_thenReturnOk() throws Exception {
        Mockito.when(itemRequestService.getAllRequests(
                Mockito.any(),
                Mockito.anyInt(),
                Mockito.anyInt()
        )).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests_whenInvalidFromValue_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "invalid")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestById_whenInvalidIdFormat_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests/invalid")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createRequest_whenNullDescription_thenReturnOk() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription(null);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        ItemRequestDto responseDto = new ItemRequestDto();
        responseDto.setId(1L);
        responseDto.setDescription(null);
        responseDto.setRequestorId(1L);
        responseDto.setCreated(LocalDateTime.now());
        responseDto.setItems(Collections.emptyList());

        Mockito.when(itemRequestService.createRequest(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllRequests_whenMissingUserIdHeader_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestsByRequestor_whenMissingUserIdHeader_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestById_whenMissingUserIdHeader_thenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isBadRequest());
    }
}