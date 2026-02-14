package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    @NotBlank(message = "Описание запроса не может быть пустым")
    private String description;
    private LocalDateTime created;
}
