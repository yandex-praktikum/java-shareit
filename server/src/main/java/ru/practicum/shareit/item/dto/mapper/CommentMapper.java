package ru.practicum.shareit.item.dto.mapper;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.RequestCreateCommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

public class CommentMapper {
    public static Comment fromDto(RequestCreateCommentDto commentDto) {
        return new Comment(null, commentDto.getText(), null, null, null);
    }

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }

    public static Collection<CommentDto> toDto(Collection<Comment> comments) {
        return comments.stream().map(CommentMapper::toDto).toList();
    }
}
