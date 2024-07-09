package ru.practicum.shareit.request;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    @NonNull
    private Long id;
    private String desc;
    private Long requesterId;
    private LocalDateTime created;
}
