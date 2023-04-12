package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@AllArgsConstructor
public class Item {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean availabilityStatus;
    private Long ownerId;
    @NotNull
    private Long requestId;
}
