package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemDtoMapperTest {

    @Test
    void toItemFromItemDtoTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        ItemDto itemDto = ItemDto.builder().id(1).name("name").description("description").available(true)
                .comments(List.of(CommentDto.builder().id(10).text("comment from author")
                        .authorName("comment author").created(now.minusMinutes(1)).build()))
                .lastBooking(new BookingShortDto(1, 2, now.minusHours(1), now))
                .nextBooking(new BookingShortDto(2, 2, now, now.plusHours(1)))
                .requestId(0).build();

        Item item = ItemDtoMapper.toItem(itemDto);

        assertEquals(1, item.getId());
        assertEquals("name", item.getName());
        assertEquals("description", item.getDescription());
        assertEquals(true, item.getAvailable());
    }

    @Test
    void toItemFromItemDtoWhenNullTest() {
        Item item = ItemDtoMapper.toItem((ItemDto) null);
        assertNull(item);
    }

    @Test
    void toItemDtoTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        User itemOwner = new User(1, "name", "e@mail.ru");
        User itemBooker = new User(2, "booker", "booker@mail.ru");
        Item item = Item.builder().id(1).name("item name").description("item description")
                .available(true).ownerId(1).build();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(Comment.builder().id(1).text("item comment 1").item(item)
                .author(itemOwner).created(now.minusMinutes(10)).build());
        commentList.add(Comment.builder().id(2).text("item comment 2").item(item)
                .author(itemOwner).created(now.minusMinutes(1)).build());

        Booking lastBooking = Booking.builder().id(1).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.minusHours(5)).rentEndDate(now.minusHours(4)).build();
        Booking nextBooking = Booking.builder().id(2).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.plusHours(5)).rentEndDate(now.plusHours(6)).build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        item.setComments(commentList);

        ItemDto itemDto = ItemDtoMapper.toItemDto(item);

        assertEquals(1, itemDto.getId());
        assertEquals("item name", itemDto.getName());
        assertEquals("item description", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());

        assertEquals(1, itemDto.getLastBooking().getId());
        assertEquals(2, itemDto.getLastBooking().getBookerId());
        assertEquals(now.minusHours(5), itemDto.getLastBooking().getRentStartTime());
        assertEquals(now.minusHours(4), itemDto.getLastBooking().getRentEndTime());

        assertEquals(2, itemDto.getNextBooking().getId());
        assertEquals(2, itemDto.getNextBooking().getBookerId());
        assertEquals(now.plusHours(5), itemDto.getNextBooking().getRentStartTime());
        assertEquals(now.plusHours(6), itemDto.getNextBooking().getRentEndTime());

        assertEquals(2, itemDto.getComments().size());
    }

    @Test
    void toItemDtoFromItemRequestTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        User itemOwner = new User(1, "name", "e@mail.ru");
        User itemBooker = new User(2, "booker", "booker@mail.ru");
        Item item = Item.builder().id(1).name("item name").description("item description")
                .available(true).ownerId(1).build();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(Comment.builder().id(1).text("item comment 1").item(item)
                .author(itemOwner).created(now.minusMinutes(10)).build());
        commentList.add(Comment.builder().id(2).text("item comment 2").item(item)
                .author(itemOwner).created(now.minusMinutes(1)).build());

        Booking lastBooking = Booking.builder().id(1).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.minusHours(5)).rentEndDate(now.minusHours(4)).build();
        Booking nextBooking = Booking.builder().id(2).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.plusHours(5)).rentEndDate(now.plusHours(6)).build();

        ItemRequest itemRequest = ItemRequest.builder().requestId(12).requestAuthor(itemBooker)
                .description("request").created(now.minusHours(10)).build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        item.setComments(commentList);
        item.setItemRequest(itemRequest);

        ItemDto itemDto = ItemDtoMapper.toItemDto(item);

        assertEquals(1, itemDto.getId());
        assertEquals("item name", itemDto.getName());
        assertEquals("item description", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());

        assertEquals(1, itemDto.getLastBooking().getId());
        assertEquals(2, itemDto.getLastBooking().getBookerId());
        assertEquals(now.minusHours(5), itemDto.getLastBooking().getRentStartTime());
        assertEquals(now.minusHours(4), itemDto.getLastBooking().getRentEndTime());

        assertEquals(2, itemDto.getNextBooking().getId());
        assertEquals(2, itemDto.getNextBooking().getBookerId());
        assertEquals(now.plusHours(5), itemDto.getNextBooking().getRentStartTime());
        assertEquals(now.plusHours(6), itemDto.getNextBooking().getRentEndTime());

        assertEquals(2, itemDto.getComments().size());

        assertEquals(12, itemDto.getRequestId());
    }

    @Test
    void toItemDtoWhenNullTest() {
        ItemDto itemDto = ItemDtoMapper.toItemDto(null);
        assertNull(itemDto);
    }

    @Test
    void toItemDtoListTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        User itemOwner = new User(1, "name", "e@mail.ru");
        User itemBooker = new User(2, "booker", "booker@mail.ru");
        Item item = Item.builder().id(1).name("item name").description("item description")
                .available(true).ownerId(1).build();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(Comment.builder().id(1).text("item comment 1").item(item)
                .author(itemOwner).created(now.minusMinutes(10)).build());
        commentList.add(Comment.builder().id(2).text("item comment 2").item(item)
                .author(itemOwner).created(now.minusMinutes(1)).build());

        Booking lastBooking = Booking.builder().id(1).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.minusHours(5)).rentEndDate(now.minusHours(4)).build();
        Booking nextBooking = Booking.builder().id(2).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.plusHours(5)).rentEndDate(now.plusHours(6)).build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        item.setComments(commentList);

        List<ItemDto> itemDtoList = ItemDtoMapper.toItemDtoList(List.of(item));
        ItemDto itemDto = itemDtoList.get(0);

        assertEquals(1, itemDto.getId());
        assertEquals("item name", itemDto.getName());
        assertEquals("item description", itemDto.getDescription());
        assertEquals(true, itemDto.getAvailable());

        assertEquals(1, itemDto.getLastBooking().getId());
        assertEquals(2, itemDto.getLastBooking().getBookerId());
        assertEquals(now.minusHours(5), itemDto.getLastBooking().getRentStartTime());
        assertEquals(now.minusHours(4), itemDto.getLastBooking().getRentEndTime());

        assertEquals(2, itemDto.getNextBooking().getId());
        assertEquals(2, itemDto.getNextBooking().getBookerId());
        assertEquals(now.plusHours(5), itemDto.getNextBooking().getRentStartTime());
        assertEquals(now.plusHours(6), itemDto.getNextBooking().getRentEndTime());

        assertEquals(2, itemDto.getComments().size());
    }

    @Test
    void toItemFromItemCreateRequestTest() {
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest("name", "description", true, 0);
        Item item = ItemDtoMapper.toItem(itemCreateRequest);

        assertEquals("name", item.getName());
        assertEquals("description", item.getDescription());
        assertEquals(true, item.getAvailable());
    }

    @Test
    void toItemFromItemCreateRequestWhenNullTest() {
        Item item = ItemDtoMapper.toItem((ItemCreateRequest) null);
        assertNull(item);
    }

    @Test
    void toItemSmallDtoTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        User itemOwner = new User(1, "name", "e@mail.ru");
        User itemBooker = new User(2, "booker", "booker@mail.ru");
        Item item = Item.builder().id(1).name("item name").description("item description")
                .available(true).ownerId(1).build();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(Comment.builder().id(1).text("item comment 1").item(item)
                .author(itemOwner).created(now.minusMinutes(10)).build());
        commentList.add(Comment.builder().id(2).text("item comment 2").item(item)
                .author(itemOwner).created(now.minusMinutes(1)).build());

        Booking lastBooking = Booking.builder().id(1).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.minusHours(5)).rentEndDate(now.minusHours(4)).build();
        Booking nextBooking = Booking.builder().id(2).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.plusHours(5)).rentEndDate(now.plusHours(6)).build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        item.setComments(commentList);

        ItemSmallDto itemSmallDto = ItemDtoMapper.toItemSmallDto(item);

        assertEquals(1, itemSmallDto.getId());
        assertEquals("item name", itemSmallDto.getName());
        assertEquals("item description", itemSmallDto.getDescription());
        assertEquals(true, itemSmallDto.getAvailable());
        assertEquals(0, itemSmallDto.getRequestId());
    }

    @Test
    void toItemSmallDtoWithItemRequestTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        User itemOwner = new User(1, "name", "e@mail.ru");
        User itemBooker = new User(2, "booker", "booker@mail.ru");
        Item item = Item.builder().id(1).name("item name").description("item description")
                .available(true).ownerId(1).build();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(Comment.builder().id(1).text("item comment 1").item(item)
                .author(itemOwner).created(now.minusMinutes(10)).build());
        commentList.add(Comment.builder().id(2).text("item comment 2").item(item)
                .author(itemOwner).created(now.minusMinutes(1)).build());

        Booking lastBooking = Booking.builder().id(1).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.minusHours(5)).rentEndDate(now.minusHours(4)).build();
        Booking nextBooking = Booking.builder().id(2).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.plusHours(5)).rentEndDate(now.plusHours(6)).build();

        ItemRequest itemRequest = ItemRequest.builder().requestId(12).requestAuthor(itemBooker)
                .description("request").created(now.minusHours(10)).build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        item.setComments(commentList);
        item.setItemRequest(itemRequest);

        ItemSmallDto itemSmallDto = ItemDtoMapper.toItemSmallDto(item);

        assertEquals(1, itemSmallDto.getId());
        assertEquals("item name", itemSmallDto.getName());
        assertEquals("item description", itemSmallDto.getDescription());
        assertEquals(true, itemSmallDto.getAvailable());
        assertEquals(12, itemSmallDto.getRequestId());
    }

    @Test
    void toItemSmallDtoWhenNullTest() {
        ItemSmallDto itemSmallDto = ItemDtoMapper.toItemSmallDto(null);
        assertNull(itemSmallDto);
    }

    @Test
    void toItemSmallDtoListTest() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        User itemOwner = new User(1, "name", "e@mail.ru");
        User itemBooker = new User(2, "booker", "booker@mail.ru");
        Item item = Item.builder().id(1).name("item name").description("item description")
                .available(true).ownerId(1).build();

        List<Comment> commentList = new ArrayList<>();
        commentList.add(Comment.builder().id(1).text("item comment 1").item(item)
                .author(itemOwner).created(now.minusMinutes(10)).build());
        commentList.add(Comment.builder().id(2).text("item comment 2").item(item)
                .author(itemOwner).created(now.minusMinutes(1)).build());

        Booking lastBooking = Booking.builder().id(1).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.minusHours(5)).rentEndDate(now.minusHours(4)).build();
        Booking nextBooking = Booking.builder().id(2).item(item).booker(itemBooker).status(BookingStatus.APPROVED)
                .rentStartDate(now.plusHours(5)).rentEndDate(now.plusHours(6)).build();

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);
        item.setComments(commentList);

        List<ItemSmallDto> itemSmallDtoList = ItemDtoMapper.toItemSmallDtoList(List.of(item));
        ItemSmallDto itemSmallDto = itemSmallDtoList.get(0);

        assertEquals(1, itemSmallDto.getId());
        assertEquals("item name", itemSmallDto.getName());
        assertEquals("item description", itemSmallDto.getDescription());
        assertEquals(true, itemSmallDto.getAvailable());
        assertEquals(0, itemSmallDto.getRequestId());
    }
}