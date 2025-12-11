package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ru.practicum.shareit.item.ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Test
    void createItem_whenValidData_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель");
        responseDto.setDescription("Мощная дрель");
        responseDto.setAvailable(true);

        Mockito.when(itemService.createItem(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"));
    }

    @Test
    void createItem_whenMissingName_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("");
        itemDto.setDescription("Описание");
        itemDto.setAvailable(true);

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("");
        responseDto.setDescription("Описание");
        responseDto.setAvailable(true);

        Mockito.when(itemService.createItem(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createItem_whenMissingDescription_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("");
        itemDto.setAvailable(true);

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель");
        responseDto.setDescription("");
        responseDto.setAvailable(true);

        Mockito.when(itemService.createItem(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createItem_whenMissingAvailable_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Описание");

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель");
        responseDto.setDescription("Описание");
        responseDto.setAvailable(null);

        Mockito.when(itemService.createItem(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    void createItemWithRequest_whenValidData_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);
        itemDto.setRequestId(1L);

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель");
        responseDto.setDescription("Мощная дрель");
        responseDto.setAvailable(true);
        responseDto.setRequestId(1L);

        Mockito.when(itemService.createItem(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.requestId").value(1));
    }

    @Test
    void updateItem_whenValidData_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель Updated");
        itemDto.setDescription("Обновленное описание");

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель Updated");
        responseDto.setDescription("Обновленное описание");
        responseDto.setAvailable(true);

        Mockito.when(itemService.updateItem(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель Updated"));
    }

    @Test
    void getItemById_whenValidRequest_thenReturnOk() throws Exception {
        ItemDto responseDto = new ItemDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель");
        responseDto.setDescription("Мощная дрель");
        responseDto.setAvailable(true);

        Mockito.when(itemService.getItemById(Mockito.any(), Mockito.any()))
                .thenReturn(responseDto);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"));
    }

    @Test
    void getItemsByOwner_whenValidRequest_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Дрель");

        List<ItemDto> items = List.of(itemDto);

        Mockito.when(itemService.getItemsByOwner(Mockito.any()))
                .thenReturn(items);

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    void searchItems_whenValidRequest_thenReturnOk() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Дрель");
        itemDto.setAvailable(true);

        List<ItemDto> items = List.of(itemDto);

        Mockito.when(itemService.searchItems(Mockito.any(), Mockito.any()))
                .thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "дрель"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    void searchItems_whenEmptyText_thenReturnOk() throws Exception {
        Mockito.when(itemService.searchItems(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void createItem_whenMissingUserIdHeader_thenReturnBadRequest() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Дрель");
        itemDto.setDescription("Мощная дрель");
        itemDto.setAvailable(true);

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_whenNotOwner_thenReturnNotFound() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Новое название");

        String jsonRequest = objectMapper.writeValueAsString(itemDto);

        Mockito.when(itemService.updateItem(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenThrow(new ru.practicum.shareit.exception.EntityNotFoundException("Вещь не найдена"));

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchItems_whenNoAvailableItems_thenReturnEmptyList() throws Exception {
        Mockito.when(itemService.searchItems(Mockito.any(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "несуществующий"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addComment_whenValidRequest_thenReturnOk() throws Exception {
        String commentJson = "{\"text\": \"Отличная дрель!\"}";

        ru.practicum.shareit.comment.dto.CommentDto commentDto =
                new ru.practicum.shareit.comment.dto.CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Отличная дрель!");

        Mockito.when(commentService.createComment(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Отличная дрель!"));
    }

    @Test
    void addComment_whenEmptyText_thenReturnOk() throws Exception {
        String commentJson = "{\"text\": \"\"}";

        ru.practicum.shareit.comment.dto.CommentDto commentDto =
                new ru.practicum.shareit.comment.dto.CommentDto();
        commentDto.setId(1L);
        commentDto.setText("");

        Mockito.when(commentService.createComment(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk());
    }
}
