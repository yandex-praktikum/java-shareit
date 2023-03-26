package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.Constants;
import ru.practicum.shareit.request.dto.ItemRequestCreateRequest;
import ru.practicum.shareit.request.dto.ItemRequestDtoMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createItemRequestEndpointTest() throws Exception {
        Mockito
                .when(itemRequestService.create(any(ItemRequest.class), anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemRequest itemRequest = invocationOnMock.getArgument(0, ItemRequest.class);
                    long requestAuthor = invocationOnMock.getArgument(1, Long.class);
                    itemRequest.setRequestId(1);
                    return itemRequest;
                });

        ItemRequestCreateRequest itemRequestCreateRequest = new ItemRequestCreateRequest("хочу вещь 1");

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("хочу вещь 1")));
    }

    @Test
    void createItemRequestEndpointExceptionTest() throws Exception {
        ItemRequestCreateRequest itemRequestCreateRequest = new ItemRequestCreateRequest("хочу вещь 1");

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnItemRequestsEndpointTest() throws Exception {
        ItemRequest itemRequest = ItemRequest.builder()
                .requestId(1)
                .description("хочу вещь 1")
                .created(LocalDateTime.now()).build();
        Mockito
                .when(itemRequestService.getOwnItemRequests(anyLong()))
                .thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ItemRequestDtoMapper.toItemRequestDtoList(List.of(itemRequest)))));
    }

    @Test
    void getAllItemRequestsEndpointTest() throws Exception {
        ItemRequest itemRequest = ItemRequest.builder()
                .requestId(1)
                .description("хочу вещь 1")
                .created(LocalDateTime.now()).build();
        Mockito
                .when(itemRequestService.getAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ItemRequestDtoMapper.toItemRequestDtoList(List.of(itemRequest)))));
    }

    @Test
    void getItemRequestEndpointTest() throws Exception {
        ItemRequest itemRequest = ItemRequest.builder()
                .requestId(1)
                .description("хочу вещь 1")
                .created(LocalDateTime.now()).build();
        Mockito
                .when(itemRequestService.getById(anyLong(), anyLong()))
                .thenReturn(itemRequest);

        mvc.perform(get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("хочу вещь 1")));
    }
}