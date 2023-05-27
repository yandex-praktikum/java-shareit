package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class ComentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getItem(),
                AuthorMapper.toAuthor(comment.getAuthor()),
                comment.getAuthor().getName(),
                comment.getDateCreated());
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(null,
                commentDto.getText(),
                commentDto.getItem(),
                AuthorMapper.toUser(commentDto.getAuthor()),
                commentDto.getCreated());
    }

    public static List<CommentDto> commentDtoList(List<Comment> commentList) {
        return commentList.stream()
                .map(ComentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
