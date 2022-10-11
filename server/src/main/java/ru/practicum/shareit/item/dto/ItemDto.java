package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemDto {
    private Integer id;

    @NotBlank(message = "name не может быть пустым")
    private String name;

    @NotBlank(message = "description не может быть пустым")
    private String description;

    @NotBlank(message = "available не может быть пустым")
    private Boolean available;

    @NotBlank(message = "owner не может быть пустым")
    private Integer owner;

    private Integer requestId;

}
