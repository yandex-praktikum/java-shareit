package shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithCommentDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShareItServer.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    private final ItemDto item = ItemDto.builder()
            .id(1L)
            .name("name1")
            .description("description1")
            .available(true)
            .build();

    private final ItemWithCommentDto itemWithComment = ItemWithCommentDto.builder()
            .id(1L)
            .name("name1")
            .description("description1")
            .available(true)
            .build();

    private final CommentDto comment = CommentDto.builder()
            .id(1L)
            .text("text1")
            .authorName("user1")
            .build();

    @Test
    public void createItem() throws Exception {
        when(itemService.createItem(eq(1L), any(ItemDto.class))).thenReturn(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));

        verify(itemService, times(1)).createItem(eq(1L), any(ItemDto.class));
    }

    @Test
    public void getItem() throws Exception {
        when(itemService.getItem(1L)).thenReturn(itemWithComment);

        mvc.perform(get("/items/{itemId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithComment.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemWithComment.getName())))
                .andExpect(jsonPath("$.description", is(itemWithComment.getDescription())))
                .andExpect(jsonPath("$.available", is(itemWithComment.getAvailable())));

        verify(itemService, times(1)).getItem(1L);
    }

    @Test
    public void updateItem() throws Exception {
        when(itemService.updateItem(eq(1L), eq(1L), any(ItemDto.class))).thenReturn(item);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));

        verify(itemService, times(1)).updateItem(eq(1L), eq(1L), any(ItemDto.class));
    }

    @Test
    public void getUserItems() throws Exception {
        when(itemService.getUserItems(1L)).thenReturn(List.of(itemWithComment));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemWithComment.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemWithComment.getName())))
                .andExpect(jsonPath("$[0].description", is(itemWithComment.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemWithComment.getAvailable())));

        verify(itemService, times(1)).getUserItems(1L);
    }

    @Test
    public void searchItems() throws Exception {
        when(itemService.searchItems(eq(1L), eq("drill"))).thenReturn(List.of(item));

        mvc.perform(get("/items/search")
                        .param("text", "drill")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())));

        verify(itemService, times(1)).searchItems(1L, "drill");
    }

    @Test
    public void createComment() throws Exception {
        when(commentService.createdComment(eq(1L), eq(1L), any(CommentDto.class))).thenReturn(comment);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())));

        verify(commentService, times(1)).createdComment(eq(1L), eq(1L), any(CommentDto.class));
    }
}
