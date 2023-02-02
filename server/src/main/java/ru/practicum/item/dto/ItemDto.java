package ru.practicum.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Positive;

@Data
@Builder
@Jacksonized
public class ItemDto {
    @Positive
    Long id;

    Long ownerId;

    String name;

    String description;

    Boolean available;

    Long requestId;
}