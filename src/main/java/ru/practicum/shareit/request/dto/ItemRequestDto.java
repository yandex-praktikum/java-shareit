package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    private Integer id;
    private String description;
    private Integer requestor;
    private LocalDateTime created;
}
