package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemSmallDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ItemRequestDto {
    /**
     * id запроса в системе, уникальное
     */
    private long id;

    /**
     * текст запроса
     */
    @NotNull
    private String description;

    private LocalDateTime created;

    private List<ItemSmallDto> items;

}