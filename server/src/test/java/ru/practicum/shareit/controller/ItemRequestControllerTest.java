package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.item.ItemInItemRequestDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Set;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDto;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Set<ItemInItemRequestDto> someItems = Set.of(new ItemInItemRequestDto(1L, "Some Item", 1L));

    @BeforeEach
    void setUp() {
        itemRequestDto =
                new ItemRequestDto(
                        1L,
                        "ItemRequest description",
                        LocalDateTime.now());
    }

    @Test
    void createNewRequest_requestShouldBeCreated() throws Exception {
        Long userId = 1L;
        RequestItemRequestDto request = new RequestItemRequestDto("");

        when(itemRequestService.create(eq(userId), any(RequestItemRequestDto.class))).thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.created")
                        .value(itemRequestDto.getCreated().format(dateTimeFormatter)));
    }

    @Test
    void createNewRequest_shouldReturnBadRequest_whenUserIdNotProvided() throws Exception {
        Long userId = 1L;
        RequestItemRequestDto request = new RequestItemRequestDto("");

        when(itemRequestService.create(eq(userId), any(RequestItemRequestDto.class))).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllRequestsOfUser_shouldReturnRequestsOfUser() throws Exception {
        Long userId = 1L;

        itemRequestDto.setResponses(someItems);
        when(itemRequestService.findAllRequestsOfUser(userId)).thenReturn(Collections.singleton(itemRequestDto));

        var item = someItems.iterator().next();
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated().format(dateTimeFormatter)))
                .andExpect(jsonPath("$[0].items").exists())
                .andExpect(jsonPath("$[0].items").isNotEmpty())
                .andExpect(jsonPath("$[0].items[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].items[0].name").value(item.getName()))
                .andExpect(jsonPath("$[0].items[0].ownerId").value(item.getOwnerId()));
    }

    @Test
    void getAllRequestsOfUser_shouldReturnBadRequest_whenUserIdNotProvided() throws Exception {
        Long userId = 1L;

        when(itemRequestService.findAllRequestsOfUser(userId)).thenReturn(Collections.singleton(itemRequestDto));

        mockMvc.perform(get("/requests"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_shouldReturnRequestsOfUser() throws Exception {
        Long userId = 1L;

        itemRequestDto.setResponses(someItems);
        when(itemRequestService.findAll(userId)).thenReturn(Collections.singleton(itemRequestDto));

        var item = someItems.iterator().next();

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated().format(dateTimeFormatter)))
                .andExpect(jsonPath("$[0].items").exists())
                .andExpect(jsonPath("$[0].items").isNotEmpty())
                .andExpect(jsonPath("$[0].items[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].items[0].name").value(item.getName()))
                .andExpect(jsonPath("$[0].items[0].ownerId").value(item.getOwnerId()));
        ;
    }

    @Test
    void getAll_shouldReturnBadRequest_whenUserIdNotProvided() throws Exception {
        Long userId = 1L;

        when(itemRequestService.findAll(userId)).thenReturn(Collections.singleton(itemRequestDto));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getById_shouldReturnRequest() throws Exception {
        long requestId = 1;

        itemRequestDto.setResponses(someItems);
        when(itemRequestService.findItemRequestBy(eq(requestId))).thenReturn(itemRequestDto);

        ItemInItemRequestDto requestDto = someItems.iterator().next();

        mockMvc.perform(get("/requests/" + requestId))
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.created").value(itemRequestDto.getCreated().format(dateTimeFormatter)))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.items[0].id").value(requestDto.getId()))
                .andExpect(jsonPath("$.items[0].name").value(requestDto.getName()))
                .andExpect(jsonPath("$.items[0].ownerId").value(requestDto.getOwnerId()));
    }

    @Test
    void getById_shouldThrowNotFoundException() throws Exception {
        long requestId = 1;
        String exMessage = format("ItemRequest по id=%d не найден", requestId);
        when(itemRequestService.findItemRequestBy(eq(requestId)))
                .thenThrow(new NotFoundException(exMessage));

        mockMvc.perform(get("/requests/" + requestId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Запрашиваемый ресурс не найден"))
                .andExpect(jsonPath("$.description").value(exMessage));
    }
}
