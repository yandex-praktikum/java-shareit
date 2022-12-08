package ru.practicum.shareit.comment;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CommentDtoFromRequest {
    @NotNull
    @NotEmpty
    private String text;
}