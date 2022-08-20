package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mopel.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public Comment toComment(CommentDto commentDto, Item item, User author) {
        Long id = commentDto.getId();
        String text = commentDto.getText();
        LocalDateTime created = commentDto.getCreated();
        return new Comment(id, text, created, item, author);
    }

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor()
                       .getName(),
                comment.getCreated());
    }

}
