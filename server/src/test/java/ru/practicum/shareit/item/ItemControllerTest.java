package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void getItemsByOwner_shouldReturnItems() throws Exception {
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        when(itemService.getItemsByOwner(1L))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Дрель"))
                .andExpect(jsonPath("$[0].available").value(true));

        verify(itemService).getItemsByOwner(1L);
    }

    @Test
    void getItemById_shouldReturnItem() throws Exception {
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        when(itemService.getById(1L, 2L))
                .thenReturn(item);

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description")
                        .value("Аккумуляторная дрель"));

        verify(itemService).getById(1L, 2L);
    }

    @Test
    void search_shouldReturnItems() throws Exception {
        ItemDto item = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        when(itemService.search("дрель"))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "дрель"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Дрель"));

        verify(itemService).search("дрель");
    }

    @Test
    void createItem_withRequestId_shouldCreateItem() throws Exception {
        ItemCreateDto requestDto = new ItemCreateDto();
        requestDto.setName("Дрель");
        requestDto.setDescription("Аккумуляторная дрель");
        requestDto.setAvailable(true);
        requestDto.setRequestId(10L);

        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Аккумуляторная дрель")
                .available(true)
                .build();

        when(itemService.create(2L, requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"));

        verify(itemService).create(2L, requestDto);
    }

    @Test
    void createItem_withoutRequestId_shouldCreateItem() throws Exception {
        ItemCreateDto requestDto = new ItemCreateDto();
        requestDto.setName("Молоток");
        requestDto.setDescription("Обычный молоток");
        requestDto.setAvailable(true);

        ItemDto responseDto = ItemDto.builder()
                .id(2L)
                .name("Молоток")
                .description("Обычный молоток")
                .available(true)
                .build();

        when(itemService.create(2L, requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Молоток"));

        verify(itemService).create(2L, requestDto);
    }

    @Test
    void updateItem_shouldReturnUpdatedItem() throws Exception {
        ItemDto requestDto = ItemDto.builder()
                .name("Новая дрель")
                .description("Новое описание")
                .available(false)
                .build();

        ItemDto responseDto = ItemDto.builder()
                .id(1L)
                .name("Новая дрель")
                .description("Новое описание")
                .available(false)
                .build();

        when(itemService.update(2L, 1L, requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Новая дрель"))
                .andExpect(jsonPath("$.available").value(false));

        verify(itemService).update(2L, 1L, requestDto);
    }

    @Test
    void addComment_shouldReturnComment() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setText("Отличная вещь!");

        CommentDto responseDto = new CommentDto();
        responseDto.setId(1L);
        responseDto.setText("Отличная вещь!");

        when(itemService.addComment(2L, 1L, requestDto))
                .thenReturn(responseDto);

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text")
                        .value("Отличная вещь!"));

        verify(itemService).addComment(2L, 1L, requestDto);
    }
}