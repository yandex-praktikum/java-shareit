package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {

    @Positive(message = "id предмета должно быть больше 0")
    private Integer id;

    @NotBlank(message = "Название предмета не может быть пустым.")
    private String name;

    @NotBlank(message = "Описание предмета не может быть пустым.")
    private String description;

    @NonNull
    private Boolean available;

    @NonNull
    private final Integer ownerId;

    private Integer requestId;

}
