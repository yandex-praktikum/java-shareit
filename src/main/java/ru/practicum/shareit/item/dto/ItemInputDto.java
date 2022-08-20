package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validator.Marker.*;
import ru.practicum.shareit.validator.NullOrNotBlank;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemInputDto {
    Long id;
    @NotBlank(groups = OnCreate.class, message = "The name should not be null or blank.")
    @NullOrNotBlank(groups = OnUpdate.class, message = "The name should be null or not blank.")
    String name;
    @NotBlank(groups = OnCreate.class, message = "Description should not be null or blank.")
    @NullOrNotBlank(groups = OnUpdate.class, message = "Description should be null or not blank.")
    String description;
    @NotNull(groups = OnCreate.class, message = "The available should be not null.")
    Boolean available;

}
