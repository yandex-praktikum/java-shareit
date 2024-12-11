package ru.practicum.shareit.item.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private boolean available;
    private LocalDateTime nextBooking;
    private LocalDateTime lastBooking;
    private Collection<CommentDto> comments;
}
