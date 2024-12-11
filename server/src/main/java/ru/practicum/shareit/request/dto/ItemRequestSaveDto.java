package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemRequestSaveDto {
    @NotBlank
    @Size(min = 1, max = 300)
    private String description;
}
