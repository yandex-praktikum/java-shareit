package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.comment.Comment;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentDtoMapper {
    public static List<CommentDto> toCommentDtoList(List<Comment> commentList) {
        List<CommentDto> commentDtoList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                commentDtoList.add(CommentDto.builder()
                        .id(comment.getId())
                        .text(comment.getText())
                        .authorName(comment.getAuthor().getName())
                        .created(comment.getCreated()).build());
            }
        }
        return commentDtoList;
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated()).build();
    }
}