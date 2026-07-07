package ru.practicum.shareit.item.comments.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private String created;
}