package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    LocalDateTime created;

    public CommentDto(String text) {
        this.text = text;
    }
}
