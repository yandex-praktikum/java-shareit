package ru.practicum.shareit.item;


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
import ru.practicum.shareit.comment.dto.AuthorDto;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectItemParameterException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.utilits.Variables;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerWebTest {
    @Mock
    ItemService itemService;

    @InjectMocks
    ItemController itemController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;
    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    private void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .setControllerAdvice(ErrorHandler.class)
                .build();

        itemDto = new ItemDto(1, "коньки", "39 размера", true, null, null, null, null);

        User owner = new User(1, "eee@email.ru", "Eva");
        Item item = new Item(1, "carpet", "description", true, null, owner);
        User author = new User(2, "sss@email.ru", "Sasha");
        AuthorDto authorDto = new AuthorDto(author.getId(), author.getName(), author.getEmail());
        commentDto = new CommentDto(1, "очень удобные", item, authorDto, authorDto.getAuthorName(), new Date());
    }

    @Test
    public void shouldSuccessAddItem() throws Exception {
        Mockito.when(itemService.addItem(Mockito.anyInt(), any()))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                .header(Variables.USER_ID, 2)

                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    public void shouldFailOnAddItemWithIncorrectParams() throws Exception {
        Mockito.when(itemService.addItem(Mockito.anyInt(), any()))
                .thenThrow(new IncorrectItemParameterException("Статус не может быть пустой"));

        mockMvc.perform(post("/items")
                .header(Variables.USER_ID, 2)

                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSuccessGetItems() throws Exception {
        Mockito.when(itemService.getItems(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                .characterEncoding(StandardCharsets.UTF_8)
                .header(Variables.USER_ID, 2)
                .param("from", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())));
    }

    @Test
    public void shouldSuccessGetItemsByText() throws Exception {
        Mockito.when(itemService.getItems(Mockito.anyString())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                .param("text", "коньки 36 размера")
                .header(Variables.USER_ID, 2)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())));
    }


    @Test
    public void shouldSuccessGetItemById() throws Exception {
        Mockito.when(itemService.getItem(Mockito.anyInt(), Mockito.anyInt())).thenReturn(itemDto);

        mockMvc.perform(get("/items/{id}", "1")
                .header(Variables.USER_ID, 2)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())));
    }

    @Test
    public void shouldSuccessUpdate() throws Exception {
        Mockito.when(itemService.update(Mockito.anyInt(), Mockito.anyInt(), any())).thenReturn(itemDto);

        itemDto.setDescription("new description");
        mockMvc.perform(patch("/items/{id}", "1")
                .header(Variables.USER_ID, 2)
                .content(mapper.writeValueAsString(itemDto))

                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.name", is(itemDto.getName())));
    }

    @Test
    public void shouldSuccessAddComment() throws Exception {
        Mockito.when(itemService.addComment(Mockito.anyInt(), Mockito.anyInt(), any())).thenReturn(commentDto);


        mockMvc.perform(post("/items/{id}/comment", "1")
                .header(Variables.USER_ID, 2)
                .content(mapper.writeValueAsString(commentDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.author.authorName", is(commentDto.getAuthor().getAuthorName())));
    }


}
