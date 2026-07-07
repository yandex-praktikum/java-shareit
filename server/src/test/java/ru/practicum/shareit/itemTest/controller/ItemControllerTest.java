package ru.practicum.shareit.itemTest.controller;

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
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comments.mapper.CommentMapper;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.comments.model.CommentDto;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.mapper.ItemMapperMapstruct;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemRequestDto;
import ru.practicum.shareit.item.model.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ItemMapperMapstruct itemMapper;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemController controller;

    private ObjectMapper mapper;
    private MockMvc mvc;
    private ItemResponseDto responseDto;
    private ItemRequestDto requestDto;
    private CommentDto commentDto;
    private Item item;
    private Comment comment;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        responseDto = new ItemResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Дрель");
        responseDto.setDescription("Аккумуляторная");
        responseDto.setAvailable(true);
        responseDto.setOwnerId(1L);
        responseDto.setComments(List.of());

        requestDto = new ItemRequestDto();
        requestDto.setName("Дрель");
        requestDto.setDescription("Аккумуляторная");
        requestDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Отличная вещь!");
        commentDto.setAuthorName("Иван");

        item = new Item();
        item.setId(1L);
        item.setName("Дрель");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Отличная вещь!");
    }

    @Test
    void createItem_shouldReturnItem() throws Exception {
        when(itemMapper.mapToItem(any(ItemRequestDto.class), isNull())).thenReturn(item);
        when(itemService.createItem(any(Item.class), eq(1L))).thenReturn(item);
        when(itemMapper.mapToDto(item)).thenReturn(responseDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Дрель")));
    }

    @Test
    void getItemById_shouldReturnItem() throws Exception {
        when(itemService.findItemById(1L)).thenReturn(item);
        when(itemMapper.mapToDto(item)).thenReturn(responseDto);

        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Дрель")));
    }

    @Test
    void deleteItem_shouldReturnOk() throws Exception {
        mvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void addComment_shouldReturnComment() throws Exception {
        when(itemService.addComment(eq(1L), eq(2L), eq("Отличная вещь!"))).thenReturn(comment);
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 2L)
                        .content("Отличная вещь!")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Отличная вещь!")));
    }
}