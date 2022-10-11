package ru.practicum.shareit.item.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class CommentDto {

    private Integer id;

    @NotBlank(message = "содержание отзыва не может быть пустым")
    private String text;

    @NotBlank(message = "id вещи не может быть пустым")
    private Integer item;

    @NotBlank(message = "id автора не может быть пустым")
    private Integer author;

    @NotBlank(message = "имя автора не может быть пустым")
    private String authorName;

}
