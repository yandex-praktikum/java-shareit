package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ShortItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FullRequestDto {
    private Long id;
    private Long userId;
    private String description;
    private LocalDateTime created;
    private List<ShortItemDto> items;
}
