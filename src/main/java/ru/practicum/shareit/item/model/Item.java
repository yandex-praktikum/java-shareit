package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@ToString
public class Item {

    private int id;
    @NotBlank(message = "Поле \'name\' не может быть пустым.")
    @NotNull(message = "Поле \'name\' не может быть пустым.")
    private String name;
    @NotBlank(message = "Поле \'description\' не может быть пустым.")
    private String description;
    @NotNull(message = "Поле \'available\' не может быть пустым.")
    private Boolean available;
    private int ownerId;
    private String request;
}
