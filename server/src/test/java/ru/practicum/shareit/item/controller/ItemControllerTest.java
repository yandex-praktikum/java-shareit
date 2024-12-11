package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentSaveDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final ItemService itemService;
    private ItemDto expectedItem;
    private CommentDto expectedComment;

    @BeforeEach
    public void initTest() {
        expectedItem = new ItemDto();
        expectedItem.setId(1);
        expectedItem.setName("item");
        expectedItem.setDescription("description");
        expectedItem.setAvailable(false);

        expectedComment = new CommentDto();
        expectedComment.setId(1);
        expectedComment.setText("comment");
        expectedComment.setAuthorName("user1");
    }

    @Test
    void addItemTest() throws Exception {
        int userId = 1;
        ItemSaveDto itemSaveDto = new ItemSaveDto();
        itemSaveDto.setName("item");
        itemSaveDto.setDescription("description");
        itemSaveDto.setAvailable(false);
        String itemSaveDtoJson = objectMapper.writeValueAsString(itemSaveDto);
        String itemDtoExpectedJson = objectMapper.writeValueAsString(expectedItem);

        when(itemService.addItem(any(Integer.class), any(ItemSaveDto.class)))
                .thenReturn(expectedItem);

        mockMvc.perform(post("/items")
                        .header(RequestHttpHeaders.USER_ID, String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(itemSaveDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDtoExpectedJson));

        verify(itemService, times(1)).addItem(any(Integer.class), any(ItemSaveDto.class));

    }

    @Test
    void addCommentTest() throws Exception {
        int userId = 1;
        int itemId = expectedItem.getId();
        String path = "/items" + "/" + itemId + "/comment";
        CommentSaveDto comment = new CommentSaveDto();
        comment.setText(expectedComment.getText());
        String commentJson = objectMapper.writeValueAsString(comment);
        String commentExpectedJson = objectMapper.writeValueAsString(expectedComment);

        when(itemService.addComment(any(Integer.class), eq(itemId), eq(comment)))
                .thenReturn(expectedComment);

        mockMvc.perform(post(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(content().json(commentExpectedJson));

        verify(itemService, times(1)).addComment(any(Integer.class), eq(itemId), eq(comment));
    }

    @Test
    void updateItemTest() throws Exception {
        int userId = 1;
        int itemId = expectedItem.getId();
        String path = "/items" + "/" + itemId;
        ItemSaveDto itemSaveDtoForUpdate = new ItemSaveDto();
        itemSaveDtoForUpdate.setDescription("updated description");
        itemSaveDtoForUpdate.setAvailable(true);
        String itemSaveDtoForUpdateJson = objectMapper.writeValueAsString(itemSaveDtoForUpdate);

        when(itemService.updateItem(eq(userId), eq(itemId), any(ItemSaveDto.class)))
                .thenAnswer(invocationOnMock -> {
                    expectedItem.setDescription(itemSaveDtoForUpdate.getDescription());
                    expectedItem.setAvailable(itemSaveDtoForUpdate.getAvailable());
                    return expectedItem;
                });

        mockMvc.perform(patch(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(itemSaveDtoForUpdateJson))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedItem)));

        verify(itemService, times(1)).updateItem(eq(userId), eq(itemId), any(ItemSaveDto.class));
    }

    @Test
    void getItemTest() throws Exception {
        int userId = 1;
        String path = "/items" + "/" + userId;

        when(itemService.getItem(eq(userId)))
                .thenReturn(expectedItem);
        String userDtoExpectedJson = objectMapper.writeValueAsString(expectedItem);

        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(itemService, times(1)).getItem(eq(userId));
    }

    @Test
    void getAllOwnerItemsTest() throws Exception {
        int userId = 1;
        List<ItemDto> itemsExpected = List.of(expectedItem);
        String itemsExpectedJson = objectMapper.writeValueAsString(itemsExpected);

        when(itemService.getAllOwnerItems(eq(userId)))
                .thenReturn(itemsExpected);

        mockMvc.perform(get("/items")
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(content().json(itemsExpectedJson));

        verify(itemService, times(1)).getAllOwnerItems(eq(userId));
    }

    @Test
    void searchItemsTest() throws Exception {
        String searchText = expectedItem.getDescription();
        List<ItemDto> itemsExpected = List.of(expectedItem);
        String itemsExpectedJson = objectMapper.writeValueAsString(itemsExpected);

        when(itemService.searchItems(eq(searchText)))
                .thenReturn(itemsExpected);

        mockMvc.perform(get("/items/search")
                        .param("text", searchText)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemsExpectedJson));

        verify(itemService, times(1)).searchItems(eq(searchText));
    }

}

