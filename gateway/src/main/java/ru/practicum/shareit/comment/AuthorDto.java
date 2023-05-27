package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorDto {
    private Integer id;
    private String authorName;
    private String email;
}
