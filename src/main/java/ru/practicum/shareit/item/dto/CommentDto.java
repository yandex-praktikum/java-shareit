package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentDto implements Serializable {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
