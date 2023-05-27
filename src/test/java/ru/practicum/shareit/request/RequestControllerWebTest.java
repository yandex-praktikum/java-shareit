package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.ErrorHandler;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.utilits.Variables;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RequestControllerWebTest {
    @Mock
    private ItemRequestService requestService;

    @InjectMocks
    private ItemRequestController requestController;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private ItemRequestDto requestDto;

    @BeforeEach
    private void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(requestController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        requestDto = new ItemRequestDto(1, "коньки для катания", null, new Date(), null);
    }

    @Test
    public void shouldSuccessAddRequest() throws Exception {
        Mockito.when(requestService.addItemRequest(Mockito.anyInt(), any()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                .header(Variables.USER_ID, 2)
                .content(mapper.writeValueAsString(requestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    public void shouldFailAddRequestWithNullDescription() throws Exception {
        Mockito.when(requestService.addItemRequest(Mockito.anyInt(), any()))
                .thenThrow(new ItemRequestNotFoundException("Описание не может быть пустым"));

        requestDto.setDescription(null);

        mockMvc.perform(post("/requests")
                .header(Variables.USER_ID, 2)
                .content(mapper.writeValueAsString(requestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSuccessGetRequests() throws Exception {
        Mockito.when(requestService.getItemRequests(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(requestDto));

        mockMvc.perform(get("/requests/all")
                .characterEncoding(StandardCharsets.UTF_8)
                .header(Variables.USER_ID, 2)
                .param("from", "1")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())));
    }

    @Test
    public void shouldFailGetRequestsByPage() throws Exception {
        mockMvc.perform(get("/requests/all")
                .characterEncoding(StandardCharsets.UTF_8)
                .param("from", "0")
                .param("size", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSuccessGetRequestById() throws Exception {
        Mockito.when(requestService.getItemRequest(Mockito.anyInt(), Mockito.anyInt())).thenReturn(requestDto);

        mockMvc.perform(get("/requests/{id}", "2")
                .header(Variables.USER_ID, 2)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSuccessGetRequestListByUserId() throws Exception {
        Mockito.when(requestService.getItemRequests(Mockito.anyInt())).thenReturn(List.of(requestDto));

        mockMvc.perform(get("/requests")
                .header(Variables.USER_ID, 2)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
