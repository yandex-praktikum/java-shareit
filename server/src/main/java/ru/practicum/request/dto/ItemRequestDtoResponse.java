package ru.practicum.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class ItemRequestDtoResponse {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;
}
