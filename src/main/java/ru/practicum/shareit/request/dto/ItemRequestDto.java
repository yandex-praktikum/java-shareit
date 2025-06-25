package ru.practicum.shareit.request.dto;

import lombok.Data;


/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequestDto {
    // private Long id;
    private String description;
    private Long requestor;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm")
    //  private LocalDateTime created;    //оставил на всякий случай
}
