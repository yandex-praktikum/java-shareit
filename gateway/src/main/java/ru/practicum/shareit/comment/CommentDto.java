package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    private Integer id;

    @NotBlank
    private String text;
    private ItemDto item;
    private AuthorDto author;
    private String authorName;
    private Date created;
}
