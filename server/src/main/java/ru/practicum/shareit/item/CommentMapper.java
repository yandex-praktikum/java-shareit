package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CommentMapper {

    default Comment mapComment(CommentDto comment, User user, Item item) {
        return Comment.builder()
                .id(comment.getId())
                .author(user)
                .text(comment.getText())
                .item(item)
                .createdAt(comment.getCreated())
                .build();
    }

    default CommentDto mapCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreatedAt())
                .build();
    }

    List<CommentDto> mapListCommentDto(List<Comment> comments);
}
