package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class ItemDto {
    private long id;
    @NotBlank
    private String name;
    private String description;
    private Boolean available;
}