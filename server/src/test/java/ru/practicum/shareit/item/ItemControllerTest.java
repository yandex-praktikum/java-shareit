package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.common.Constants;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.CommentDtoMapper;
import ru.practicum.shareit.item.dto.ItemCreateRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createItemEndpointTest() throws Exception {
        Mockito
                .when(itemService.create(any(ItemCreateRequest.class), anyLong()))
                .thenAnswer(invocationOnMock -> {
                    ItemCreateRequest itemCreateRequest = invocationOnMock.getArgument(0, ItemCreateRequest.class);
                    Item item = ItemDtoMapper.toItem(itemCreateRequest);
                    item.setId(1);
                    return item;
                });

        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("вещь 1", "очень хорошая вещь 1 почти новая", true, 0);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("вещь 1")))
                .andExpect(jsonPath("$.description", is("очень хорошая вещь 1 почти новая")));
    }

    @Test
    void createItemEndpointExceptionTest() throws Exception {
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("вещь 1", "очень хорошая вещь 1 почти новая", true, 0);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemCreateRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemEndpointTest() throws Exception {
        Mockito
                .when(itemService.update(any(Item.class), anyLong()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0, Item.class));

        ItemDto itemDto = ItemDto.builder().id(1).name("вещь 2").description("очень хорошая вещь 2").build();

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("вещь 2")))
                .andExpect(jsonPath("$.description", is("очень хорошая вещь 2")));
    }

    @Test
    void updateItemEndpointExceptionTest() throws Exception {
        ItemDto itemDto = ItemDto.builder().id(1).name("вещь 2").description("очень хорошая вещь 2").build();

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnedItemsListEndpointTest() throws Exception {
        Item item1 = Item.builder().id(1).ownerId(1).name("вещь1").description("описание 1").available(true).build();
        Item item2 = Item.builder().id(2).ownerId(1).name("вещь2").description("описание 2").available(true).build();

        Mockito
                .when(itemService.getOwnedItemsList(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(item1, item2));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ItemDtoMapper.toItemDtoList(List.of(item1, item2)))));
    }

    @Test
    void getOwnedItemsListEndpointExceptionTest() throws Exception {
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemEndpointTest() throws Exception {
        Item item1 = Item.builder().id(1).ownerId(1).name("вещь1").description("описание 1").available(true).build();

        Mockito
                .when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(item1);

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ItemDtoMapper.toItemDto(item1))));
    }

    @Test
    void searchItemsEndpointTest() throws Exception {
        Item item1 = Item.builder().id(1).ownerId(1).name("вещь 1").description("описание 1").available(true).build();
        Item item2 = Item.builder().id(2).ownerId(1).name("вещь 2").description("описание 2").available(true).build();

        Mockito
                .when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(item1, item2));

        mvc.perform(get("/items/search")
                        .param("text", "вещь")
                        .param("from", "0")
                        .param("size", "20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(ItemDtoMapper.toItemDtoList(List.of(item1, item2)))));
    }

    @Test
    void searchItemsEndpointWithEmptyTextTest() throws Exception {
        Item item1 = Item.builder().id(1).ownerId(1).name("вещь 1").description("описание 1").available(true).build();
        Item item2 = Item.builder().id(2).ownerId(1).name("вещь 2").description("описание 2").available(true).build();

        Mockito
                .when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(item1, item2));

        mvc.perform(get("/items/search")
                        .param("text", "")
                        .param("from", "0")
                        .param("size", "20")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }

    @Test
    void deleteItemEndPointTest() throws Exception {
        mvc.perform(delete("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk());
    }

    @Test
    void deleteItemEndPointExceptionTest() throws Exception {
        mvc.perform(delete("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, -1))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCommentOnItemEndpointTest() throws Exception {
        User authorUser = new User(1, "user name", "user@email.com");
        Item item1 = Item.builder().id(1).ownerId(1).name("вещь 1").description("описание 1").available(true).build();
        Comment comment = Comment.builder().id(1).text("комментарий к вещи 1").item(item1)
                .author(authorUser).created(LocalDateTime.now()).build();

        Mockito
                .when(itemService.createComment(anyString(), anyLong(), anyLong()))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(CommentDtoMapper.toCommentDto(comment)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(CommentDtoMapper.toCommentDto(comment))));
    }

    @Test
    void createCommentOnItemEndpointExceptionTest() throws Exception {
        User authorUser = new User(1, "user name", "user@email.com");
        Item item1 = Item.builder().id(1).ownerId(1).name("вещь 1").description("описание 1").available(true).build();
        Comment comment = Comment.builder().id(1).text("комментарий к вещи 1").item(item1)
                .author(authorUser).created(LocalDateTime.now()).build();

        Mockito
                .when(itemService.createComment(anyString(), anyLong(), anyLong()))
                .thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(CommentDtoMapper.toCommentDto(comment)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(Constants.X_HEADER_NAME, -1))
                .andExpect(status().isBadRequest());
    }
}