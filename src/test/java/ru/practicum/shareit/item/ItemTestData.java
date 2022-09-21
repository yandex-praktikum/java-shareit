package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static ru.practicum.shareit.user.UserTestData.user1;
import static ru.practicum.shareit.user.UserTestData.user2;

public class ItemTestData {
    public static ItemDto itemDto1 = ItemDto.builder()
            .id(1).name("item1").description("description1").available(true)
            .comments(new ArrayList<>()).build();
    public static ItemDto itemDto2 = ItemDto.builder()
            .id(2).name("item2").description("description2").available(true)
            .comments(new ArrayList<>()).build();
    public static ItemDto itemDto3 = ItemDto.builder()
            .id(3).name("item3").description("description3").available(false)
            .comments(new ArrayList<>()).build();
    public static ItemDto itemDto4 = ItemDto.builder()
            .id(4).name("item4").description("description4")
            .comments(new ArrayList<>()).available(true).build();
    public static ItemDto itemDtoCreated = ItemDto.builder()
            .id(5).name("itemCreated").description("descriptionCreated").available(true)
            .comments(new ArrayList<>()).build();

    public static CommentDto commentDto = CommentDto.builder().id(1L).text("comment")
            .itemId(1L).authorName("user2").created(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE)).build();

    public static Item item1 = Item.builder().id(1L).name("item1").description("description1")
            .available(true).owner(user1).build();
    public static Item item2 = Item.builder().id(2L).name("item2").description("description2")
            .available(true).owner(user1).build();
    public static Item item3 = Item.builder().id(3L).name("item3").description("description3")
            .available(false).owner(user1).build();
    public static Item item4 = Item.builder().id(4L).name("item4").description("description4")
            .available(true).owner(user2).build();
}
