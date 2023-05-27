package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

public interface CommentService {
    ItemDto addComment(Integer authorId, Integer itemId, CommentDto commentDto);
}
