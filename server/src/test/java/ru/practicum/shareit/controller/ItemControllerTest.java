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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.RequestCreateCommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.dto.item.RequestItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private CommentDto comment1;
    private CommentDto comment2;

    @BeforeEach
    void setUp() {
        comment1 = new CommentDto(
                1L,
                "Text of comment-1", "Some name of author-1",
                LocalDateTime.now()
        );

        comment2 = new CommentDto(
                2L,
                "Text of comment-2", "Some name of author-2",
                LocalDateTime.now().minusDays(1)
        );

        List<CommentDto> comments = List.of(comment1, comment2);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Description of Test Item");
        itemDto.setStatus(true);
        itemDto.setComments(comments);
    }

    @Test
    void findAllItemsOfUser_shouldReturnItemList() throws Exception {
        when(itemService.findAllItemsByUserId(1L)).thenReturn(Collections.singletonList(itemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getStatus()))
                .andExpect(jsonPath("$[0].comments[*].text",
                        containsInAnyOrder(itemDto.getComments().stream()
                                .map(CommentDto::getText)
                                .toArray(String[]::new))));


        verify(itemService, times(1)).findAllItemsByUserId(1L);
    }

    @Test
    void findAllItemsOfUser_shouldReturnBadRequest_whenUserIdNotProvided() throws Exception {
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_shouldReturnItem() throws Exception {
        when(itemService.findItemById(1L)).thenReturn(itemDto);

        mockMvc.perform(get("/items/" + 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getStatus()))
                .andExpect(jsonPath("$.comments[*].text",
                        containsInAnyOrder(itemDto.getComments().stream()
                                .map(CommentDto::getText)
                                .toArray(String[]::new))));
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        when(itemService.findItemById(any())).thenThrow(new NotFoundException("Item по id=1 не найден"));

        mockMvc.perform(get("/items/" + 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSearchByNameAndDescription() throws Exception {
        String searchText = "item";
        List<ItemDto> items = Arrays.asList(new ItemDto(1L, "Item1", "Description1", true, null),
                new ItemDto(2L, "Item2", "Description2", true, null));

        when(itemService.searchByNameAndDescription(searchText)).thenReturn(items);

        mockMvc.perform(get("/items/search")
                        .param("text", searchText)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(items.getFirst().getId()))
                .andExpect(jsonPath("$[0].name").value(items.getFirst().getName()))
                .andExpect(jsonPath("$[1].id").value(items.getLast().getId()))
                .andExpect(jsonPath("$[1].name").value(items.getLast().getName()));
    }

    @Test
    public void createItem_itemShouldBeCreated() throws Exception {
        Long userId = 1L;
        RequestItemDto requestDto = new RequestItemDto("New Item", "New Description", true, null);
        ItemDto itemDto = new ItemDto(1L, "New Item", "New Description", true, null);

        when(itemService.create(eq(userId), any(RequestItemDto.class))).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void createItem_shouldReturnBadRequest_whenUserIdNotProvided() throws Exception {
        RequestItemDto requestDto = new RequestItemDto("New Item", "New Description", true, null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateItem_itemShouldBeUpdated() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        RequestItemDto requestDto = new RequestItemDto("Updated Item", "Updated Description", false);
        ItemDto itemDto = new ItemDto(itemId, "Updated Item", "Updated Description", false, null);

        when(itemService.update(eq(userId), eq(itemId), any(RequestItemDto.class))).thenReturn(itemDto);
        doReturn(itemDto).when(itemService).update(eq(userId), eq(itemId), any(RequestItemDto.class));
        mockMvc.perform(patch("/items/" + itemId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()));
    }

    @Test
    void updateItem_shouldReturnBadRequest_whenUserIdNotProvided() throws Exception {
        RequestItemDto requestDto = new RequestItemDto("Updated Item", "Updated Description", false);

        mockMvc.perform(patch("/items/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteItem_itemShouldBeDeleted() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteItem_shouldReturnNotFoundException() throws Exception {
        Long itemId = 1L;
        String exMessage = "Item по id=1 не найден";
        doThrow(new NotFoundException(exMessage)).when(itemService).deleteById(itemId);
        mockMvc.perform(delete("/items/" + itemId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Запрашиваемый ресурс не найден"))
                .andExpect(jsonPath("$.description").value(exMessage));
    }

    @Test
    void addCommentToItem_commentShouldBeAddedToItem() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;
        RequestCreateCommentDto commentDto = new RequestCreateCommentDto("Test Comment!");
        CommentDto comment = new CommentDto(1L, commentDto.getText(), "Author's name", LocalDateTime.now());

        when(itemService.saveCommentToItem(eq(userId), eq(itemId), any(RequestCreateCommentDto.class))).thenReturn(comment);

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.authorName").value(comment.getAuthorName()))
                .andExpect(jsonPath("$.created").value(containsString(comment.getCreated().toString().substring(0, 26))));
    }

    @Test
    void addCommentToItem_shouldReturnBadRequest_whenUserIdNotProvided() throws Exception {
        long itemId = 1L;
        RequestCreateCommentDto commentDto = new RequestCreateCommentDto("Test Comment!");

        mockMvc.perform(post("/items/" + itemId + "/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isBadRequest());
    }
}