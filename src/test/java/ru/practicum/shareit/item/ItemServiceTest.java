package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.UserIsNotBookerException;
import ru.practicum.shareit.item.exception.UserIsNotOwnerException;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.request.exception.ItemRequestNotGoodParametrsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.shareit.item.ItemTestData.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    @Autowired
    private final ItemServiceImpl itemService;

    @Test
    @DirtiesContext
    void testCreate() {
        long itemId = itemService.create(1L, itemDtoCreated).getId();
        ItemDto itemDtoFromSQL = itemService.getItemById(1L, itemId);
        assertThat(itemDtoFromSQL, equalTo(itemDtoCreated));
    }

    @Test
    @DirtiesContext
    void testCreateWithItemRequest() {
        itemDtoCreated.setRequestId(1L);
        long itemId = itemService.create(1L, itemDtoCreated).getId();
        ItemDto itemDtoFromSQL = itemService.getItemById(1L, itemId);
        assertThat(itemDtoFromSQL, equalTo(itemDtoCreated));
        itemDtoCreated.setRequestId(null);
    }

    @Test
    void testCreateWithWrongItemRequest() {
        itemDtoCreated.setRequestId(10L);
        assertThrows(ItemRequestNotFoundException.class, () -> itemService.create(1L, itemDtoCreated));
        itemDtoCreated.setRequestId(null);
    }

    @Test
    @DirtiesContext
    void testUpdate() {
        itemDto1.setName("new name");
        itemService.update(1L, 1L, itemDto1);
        ItemDto itemDtoFromSQL = itemService.getItemById(1L, 1L);
        assertThat(itemDtoFromSQL.getName(), equalTo(itemDto1.getName()));
        itemDto1.setName("item1");
    }

    @Test
    @DirtiesContext
    void testUpdateWithNulls() {
        itemDto1.setName(null);
        itemDto1.setDescription(null);
        itemDto1.setAvailable(null);
        itemService.update(1L, 1L, itemDto1);
        ItemDto itemDtoFromSQL = itemService.getItemById(1L, 1L);
        itemDto1.setName("item1");
        itemDto1.setDescription("description1");
        itemDto1.setAvailable(true);
        assertThat(itemDtoFromSQL.getName(), equalTo(itemDto1.getName()));
    }

    @Test
    @DirtiesContext
    void testUpdateDescription() {
        itemDto1.setDescription("new description");
        itemService.update(1L, 1L, itemDto1);
        ItemDto itemDtoFromSQL = itemService.getItemById(1L, 1L);
        assertThat(itemDtoFromSQL.getName(), equalTo(itemDto1.getName()));
        itemDto1.setDescription("description1");
    }

    @Test
    @DirtiesContext
    void testUpdateAvailable() {
        itemDto1.setAvailable(false);
        itemService.update(1L, 1L, itemDto1);
        ItemDto itemDtoFromSQL = itemService.getItemById(1L, 1L);
        assertThat(itemDtoFromSQL.getName(), equalTo(itemDto1.getName()));
        itemDto1.setAvailable(true);
    }

    @Test
    void testUpdateWrongItem() {
        itemDto1.setName("new name");
        assertThrows(ItemNotFoundException.class, () -> itemService.update(1L, 50L, itemDto1));
        itemDto1.setName("item1");
    }

    @Test
    void testUpdateWrongOwner() {
        itemDto1.setName("new name");
        assertThrows(UserNotFoundException.class, () -> itemService.update(10L, 1L, itemDto1));
        itemDto1.setName("item1");
    }

    @Test
    void testUpdateNotOwner() {
        assertThrows(UserIsNotOwnerException.class, () ->
                itemService.update(2L, 1L, itemDto1));
    }

    @Test
    void testGetItemById() {
        ItemDto itemDtoFromSQL = itemService.getItemById(2L, 2L);
        assertThat(itemDtoFromSQL, equalTo(itemDto2));
    }

    @Test
    void testGetItemByWrongId() {
        assertThrows(ItemNotFoundException.class, () -> itemService.getItemById(1L, 10L));
    }

    @Test
    void testGetAllItemsByUser() {
        List<ItemDto> items = itemService.getAllItemsByUser(1, 10, 2L);
        assertThat(items, equalTo(List.of(itemDto4)));
    }

    @Test
    void testGetAllItemsByWrongFrom() {
        assertThrows(ItemRequestNotGoodParametrsException.class, () -> itemService.getAllItemsByUser(-1, 10, 2L));
    }

    @Test
    void testGetAllItemsByWrongUser() {
        assertThrows(UserNotFoundException.class, () -> itemService.getAllItemsByUser(1, 10, 20L));
    }


    @Test
    void testGetAllItemsWrongFrom() {
        assertThrows(ItemRequestNotGoodParametrsException.class, () -> itemService.search(-1, 10, "item1"));
    }

    @Test
    void testSearch() {
        //этот метод выводит ItemDto без комментариев, поэтому убираем их
        itemDto1.setComments(null);
        List<ItemDto> items = itemService.search(1, 10, "item1");
        assertThat(items, equalTo(List.of(itemDto1)));
        itemDto1.setComments(new ArrayList<>());
    }

    @Test
    void testSearchWrongFrom() {
        assertThrows(ItemRequestNotGoodParametrsException.class, () -> itemService.search(-1, 10, "item1"));
    }


    @Test
    void testSearchEmpty() {
        List<ItemDto> items = itemService.search(1, 10, "");
        assertThat(items, equalTo(new ArrayList<>()));
    }

    @Test
    void testCreateItemWrongOwner() {
        assertThrows(UserNotFoundException.class, () ->
                itemService.create(500L, itemDto1));
    }

    @Test
    void testCreateEmptyComment() {
        commentDto.setText("");
        assertThrows(UserIsNotBookerException.class, () ->
                itemService.createComment(2L, 1L, commentDto));
        commentDto.setText("comment");
    }

    @Test
    void testCreateCommentWrongItem() {
        assertThrows(ItemNotFoundException.class, () ->
                itemService.createComment(2L, 10L, commentDto));
    }

    @Test
    void testCreateCommentWrongBooking() {
        assertThrows(UserIsNotBookerException.class, () ->
                itemService.createComment(3L, 1L, commentDto));
    }

    @Test
    void testCreateCommentWrongUser() {
        assertThrows(UserNotFoundException.class, () ->
                itemService.createComment(300L, 1L, commentDto));
    }

    @Test
    void testCreateCommentWrongBookingUser() {
        assertThrows(UserIsNotBookerException.class, () ->
                itemService.createComment(1L, 1L, commentDto));
    }

    @Test
    @DirtiesContext
    void testCreateComment() {
        CommentDto commentDto1 = itemService.createComment(2L, 1L, commentDto);
        assertThat(commentDto1, equalTo(commentDto));
    }

    @Test
    void testCreateCommentNotBooker() {
        assertThrows(UserIsNotBookerException.class, () -> itemService.createComment(3L, 1L, commentDto));
    }

    @Test
    void testFindCommentsByWrongItem() {
        assertThrows(ItemNotFoundException.class, () -> itemService.findCommentsByItem(34L));
    }

}