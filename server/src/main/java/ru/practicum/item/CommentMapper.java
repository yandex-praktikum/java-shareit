package ru.practicum.item;

import ru.practicum.item.dto.CommentDto;
import ru.practicum.utilities.DateUtils;


public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                null,
                null,
                DateUtils.now());
    }

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}
