package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    //@NotBlank
    private String name;
    //@NotNull
    private String description;
    //@AssertTrue
    private Boolean available;
    private Long owner;
    private Long requestId;

}
