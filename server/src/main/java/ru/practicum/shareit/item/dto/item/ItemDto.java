package ru.practicum.shareit.item.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.Collection;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;

    @JsonProperty("available")
    private Boolean status;

    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Long request;

    private Collection<CommentDto> comments;

    public ItemDto(Long id, String name, String description, Boolean status, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.request = request;
    }

    public ItemDto(Long id, String name, String description,
                   Boolean status, Long request, Collection<CommentDto> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.request = request;
        this.comments = comments;
    }
}
