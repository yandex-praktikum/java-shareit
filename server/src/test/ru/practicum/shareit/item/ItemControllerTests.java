package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exception.CommentAddException;
import ru.practicum.exception.ErrorHandler;
import ru.practicum.exception.ItemNotFoundException;
import ru.practicum.item.*;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoResponse;
import ru.practicum.request.ItemRequestRepository;
import ru.practicum.shareit.Config.ItemControllerTestConfig;
import ru.practicum.shareit.Config.WebConfig;
import ru.practicum.user.UserRepository;

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

@SpringBootTest
@SpringJUnitWebConfig({ItemController.class, ItemControllerTestConfig.class, WebConfig.class, ErrorHandler.class})
public class ItemControllerTests {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController controller;

    @Mock
    private ItemValidator validator;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private Item item;
    private ItemDto itemDto;
    private ItemDtoResponse itemDtoResponse;
    private CommentDto commentDto;


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(new ErrorHandler(), controller, validator, itemRepository, itemRequestRepository)
                .build();
        item = new Item(1L, "Item1", "description", true, 1L, null);
        commentDto = new CommentDto(1L, "txt", "name", LocalDateTime.now());
        itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(1L)
                .build();
        mapper.findAndRegisterModules();
        itemDtoResponse = ItemDtoResponse.builder()
                .id(1L)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .lastBooking(null)
                .nextBooking(null)
                .comments(null)
                .userId(1L)
                .build();
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(itemService.getItemById(any(), any()))
                .thenReturn(itemDtoResponse);

        mvc.perform(get("/items/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoResponse.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoResponse.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoResponse.getAvailable())));

        when(itemService.getItemById(any(), any()))
                .thenThrow(ItemNotFoundException.class);
        mvc.perform(get("/items/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllItemsTest() throws Exception {
        when(itemService.getAllByUserId(any()))
                .thenReturn(List.of(itemDtoResponse));

        mvc.perform(get("/items").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())));
    }

    @Test
    void createItemTest() throws Exception {

        when(itemService.add(any(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.update(any(), any(), any()))
                .thenReturn(item);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void searchItemTest() throws Exception {
        when(itemService.getItemsByText(any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search").param("text", "ddd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemDtoResponse.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoResponse.getAvailable())));
    }

    @Test
    void addCommentTest() throws Exception {
        when(itemService.addComment(any(), any(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));

        when(itemService.addComment(any(), any(), any()))
                .thenThrow(CommentAddException.class);
        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCommentTestSecurityException() throws Exception {
        when(itemService.addComment(any(), any(), any()))
                .thenThrow(CommentAddException.class);
        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTest() throws Exception {
        when(itemService.delete(any(), any()))
                .thenReturn(item);
        mvc.perform(delete("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
