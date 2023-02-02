package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exception.ErrorHandler;
import ru.practicum.exception.ItemRequestNotFoundException;
import ru.practicum.request.ItemRequestController;
import ru.practicum.request.ItemRequestService;
import ru.practicum.request.dto.ItemRequestDto;
import ru.practicum.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.Config.ItemRequestControllerTestConfig;
import ru.practicum.shareit.Config.WebConfig;
import ru.practicum.utilities.Constants;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({ItemRequestController.class,
        ItemRequestControllerTestConfig.class, WebConfig.class, ErrorHandler.class})
public class ItemRequestControllerTests {
    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemRequestDtoResponse requestDtoForResponse;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new ErrorHandler(), controller)
                .build();

        requestDto = ItemRequestDto.builder()
                .requestorId(1L)
                .description("description")
                .build();

        requestDtoForResponse = ItemRequestDtoResponse.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .items(null)
                .build();
    }

    @Test
    void createRequestTest() throws Exception {
        when(requestService.addRequest(any(), any()))
                .thenReturn(requestDtoForResponse);

        mvc.perform(post("/requests").header(Constants.USER_ID_HEADER, 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoForResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDtoForResponse.getDescription())));
    }

    @Test
    void findRequestsByRequestorTest() throws Exception {
        when(requestService.getRequestByOwnerId(any()))
                .thenReturn(List.of(requestDtoForResponse));

        mvc.perform(get("/requests").header(Constants.USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDtoForResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoForResponse.getDescription())));

        when(requestService.getRequestByOwnerId(any()))
                .thenThrow(ItemRequestNotFoundException.class);

        mvc.perform(get("/requests").header(Constants.USER_ID_HEADER, 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void findRequestsWithPaginationTest() throws Exception {
        when(requestService.getAllRequests(any(), any(), any()))
                .thenReturn(List.of(requestDtoForResponse));

        mvc.perform(get("/requests/all").header(Constants.USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDtoForResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoForResponse.getDescription())));
    }

    @Test
    void getRequestByIdTest() throws Exception {
        when(requestService.getRequestById(any(), any()))
                .thenReturn(requestDtoForResponse);
        mvc.perform(get("/requests/1").header(Constants.USER_ID_HEADER, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoForResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDtoForResponse.getDescription())));
    }
}
