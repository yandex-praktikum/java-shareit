package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private LocalDateTime created;
    private Long userId;
    private String description;
}
