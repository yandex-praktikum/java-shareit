package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestDtoMapperTest {

    @Test
    void toItemRequestDtoTest() {
        LocalDateTime createdDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusDays(1);
        User itemRequestAuthor = new User(1, "name", "e@mail.ru");
        ItemRequest itemRequest = ItemRequest.builder().requestId(1).description("текст запроса 1")
                .requestAuthor(itemRequestAuthor).created(createdDateTime).build();

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest);

        assertEquals(1, itemRequestDto.getId());
        assertEquals("текст запроса 1", itemRequestDto.getDescription());
        assertEquals(createdDateTime, itemRequestDto.getCreated());
    }

    @Test
    void toItemRequestDtoWithItemsTest() {
        LocalDateTime createdDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusDays(1);
        User itemRequestAuthor = new User(1, "name", "e@mail.ru");
        Item item = Item.builder().id(1).build();
        ItemRequest itemRequest = ItemRequest.builder().requestId(1).description("текст запроса 1")
                .requestAuthor(itemRequestAuthor).created(createdDateTime).items(List.of(item)).build();

        ItemRequestDto itemRequestDto = ItemRequestDtoMapper.toItemRequestDto(itemRequest);

        assertEquals(1, itemRequestDto.getId());
        assertEquals("текст запроса 1", itemRequestDto.getDescription());
        assertEquals(createdDateTime, itemRequestDto.getCreated());
        assertEquals(1, itemRequestDto.getItems().size());
        assertEquals(1, itemRequestDto.getItems().get(0).getId());
    }

    @Test
    void toItemRequestTest() {
        ItemRequestCreateRequest itemRequestCreateRequest = new ItemRequestCreateRequest("текст запроса 1");

        ItemRequest itemRequest = ItemRequestDtoMapper.toItemRequest(itemRequestCreateRequest);

        assertEquals("текст запроса 1", itemRequest.getDescription());
    }

    @Test
    void toItemRequestDtoList() {
        LocalDateTime createdDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).minusDays(1);
        User itemRequestAuthor = new User(1, "name", "e@mail.ru");
        ItemRequest itemRequest1 = ItemRequest.builder().requestId(1).description("текст запроса 1")
                .requestAuthor(itemRequestAuthor).created(createdDateTime).build();
        ItemRequest itemRequest2 = ItemRequest.builder().requestId(2).description("текст запроса 2")
                .requestAuthor(itemRequestAuthor).created(createdDateTime).build();

        List<ItemRequestDto> itemRequestListDto = ItemRequestDtoMapper
                .toItemRequestDtoList(List.of(itemRequest1, itemRequest2));

        assertEquals(2, itemRequestListDto.size());
        assertEquals(1, itemRequestListDto.get(0).getId());
        assertEquals("текст запроса 1", itemRequestListDto.get(0).getDescription());
        assertEquals(2, itemRequestListDto.get(1).getId());
        assertEquals("текст запроса 2", itemRequestListDto.get(1).getDescription());
    }
}