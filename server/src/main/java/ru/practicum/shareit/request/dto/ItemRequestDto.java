package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDto {

    private int id;
    @NotBlank
    private String description;
    private LocalDateTime created;
    private List<ItemDtoForRequest> items;

    public ItemRequestDto(int id, String description, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.created = created;
    }

    public ItemRequestDto() {

    }
}
