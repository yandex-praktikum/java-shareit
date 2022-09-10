package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    ItemBookingDto getItemById(long itemId, long userId);

    void delete(long userId, long itemId);

    List<ItemBookingDto> getAllUsersItems(long userId, int from, int size);

    List<ItemDto> search(String text, int from, int size);

    CommentDto createComment(CommentDto commentDto, long itemId, long userId);
}
