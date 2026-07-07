package ru.practicum.shareit.requestItemTest.controller;

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
import ru.practicum.shareit.request.answers.repository.AnswerRepository;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.mapper.RequestItemMapper;
import ru.practicum.shareit.request.model.RequestItem;
import ru.practicum.shareit.request.model.RequestItemRequestDto;
import ru.practicum.shareit.request.model.RequestItemResponseDto;
import ru.practicum.shareit.request.service.RequestItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RequestItemControllerTest {

    @Mock
    private RequestItemService requestItemService;

    @Mock
    private RequestItemMapper mapper;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private RequestController controller;

    private ObjectMapper objectMapper;
    private MockMvc mvc;
    private RequestItemResponseDto responseDto;
    private RequestItemRequestDto requestDto;
    private RequestItem requestItem;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        responseDto = RequestItemResponseDto.builder()
                .id(1L)
                .description("Нужна аккумуляторная дрель")
                .requestorId(2L)
                .answers(List.of())
                .build();

        requestDto = RequestItemRequestDto.builder()
                .description("Нужна аккумуляторная дрель")
                .build();

        User user = new User();
        user.setId(2L);

        requestItem = RequestItem.builder()
                .id(1L)
                .description("Нужна аккумуляторная дрель")
                .requestor(user)
                .build();
    }

    @Test
    void createRequestItem_shouldReturnRequest() throws Exception {
        when(mapper.mapToRequestItem(any(RequestItemRequestDto.class))).thenReturn(requestItem);
        when(requestItemService.createRequestItem(any(RequestItem.class), eq(2L))).thenReturn(requestItem);
        when(mapper.mapToDto(requestItem)).thenReturn(responseDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 2L)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Нужна аккумуляторная дрель")))
                .andExpect(jsonPath("$.requestorId", is(2)));
    }

    @Test
    void getAllRequestItemByOwner_shouldReturnList() throws Exception {
        when(requestItemService.getAllRequestItemByOwner(2L)).thenReturn(List.of(requestItem));
        when(mapper.mapToDto(requestItem)).thenReturn(responseDto);
        when(answerRepository.findByRequestId(1L)).thenReturn(List.of());

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("Нужна аккумуляторная дрель")));
    }

    @Test
    void getAllRequests_shouldReturnList() throws Exception {
        when(requestItemService.getAllRequestItemAllUsers(1L)).thenReturn(List.of(requestItem));
        when(mapper.mapToDto(requestItem)).thenReturn(responseDto);
        when(answerRepository.findByRequestId(1L)).thenReturn(List.of());

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void getRequestById_shouldReturnRequest() throws Exception {
        when(requestItemService.getRequestItemById(1L)).thenReturn(requestItem);
        when(mapper.mapToDto(requestItem)).thenReturn(responseDto);
        when(answerRepository.findByRequestId(1L)).thenReturn(List.of());

        mvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Нужна аккумуляторная дрель")));
    }
}