package ru.practicum.shareit.item.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Integer ownerId, ItemDto itemDto);

    ItemDto update(Integer ownerId, Integer itemId, ItemDto itemDto);

    CommentDto addComment(Integer authorId, Integer itemId, CommentDto commentDto);

    ItemDto getItem(Integer ownerId, Integer itemId);

    List<ItemDto> getItems(Integer ownerId);

    List<ItemDto> getItems(String text);
}
