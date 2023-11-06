package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static Comment fromCommentDto(CommentDto commentDto, Item item, User user, LocalDateTime time) {
        return new Comment(
                commentDto.getId(),
            commentDto.getText(),
            item,
            user,
            time
        );
    }

    public static CommentFullDto toCommentFullDto(Comment comment) {
        return new CommentFullDto(
                comment.getId(),
                comment.getText(),
                comment.getItem(),
                comment.getAuthor(),
                comment.getAuthor().getName(),
                comment.getCreated().toString()
        );
    }

    public static List<CommentFullDto> toCommentsFullDto(List<Comment> comments) {
        List<CommentFullDto> commentsFullDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentsFullDto.add(toCommentFullDto(comment));
        }
        return commentsFullDto;
    }
}
