package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {

    private int id;
    private String text;
    private String authorName;
    private LocalDateTime created;

    public CommentDto() {

    }
}
