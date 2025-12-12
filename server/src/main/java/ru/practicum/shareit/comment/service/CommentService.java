package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;

public interface CommentService {
    CommentDto createComment(CommentRequestDto commentRequestDto, Long itemId, Long authorId);
}
