package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentRequestDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.user.model.User;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment, User author) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                author.getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentRequestDto commentRequestDto, Long itemId, Long authorId) {
        return new Comment(
                null,
                commentRequestDto.getText(),
                itemId,
                authorId,
                null
        );
    }
}
