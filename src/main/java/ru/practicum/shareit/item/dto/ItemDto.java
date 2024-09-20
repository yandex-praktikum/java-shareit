package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.validation.Marker;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Slf4j
public class ItemDto {
    private Long id;
    @NotEmpty(groups = Marker.AddItem.class)
    @Size(min = 1, groups = Marker.UpdateItem.class)
    private String name;
    @NotEmpty(groups = Marker.AddItem.class)
    @Size(min = 1, groups = Marker.UpdateItem.class)
    private String description;
    @BooleanFlag
    @NotNull(groups = Marker.AddItem.class)
    private Boolean available;
    private Long request;
}
