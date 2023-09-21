package ru.practicum.shareit.request.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    Long id;
    @NotBlank
    String description;

    Long requesterId;

    LocalDateTime created = LocalDateTime.now();

    Boolean resolved = false;
}