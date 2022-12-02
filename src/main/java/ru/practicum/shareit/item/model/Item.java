package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    private int id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private int userId;

    @NotNull
    private Boolean available;
}
