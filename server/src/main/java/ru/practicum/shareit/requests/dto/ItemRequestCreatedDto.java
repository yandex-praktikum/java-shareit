package ru.practicum.shareit.requests.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemRequestCreatedDto {
    private String description;
    private User requestor;
    private LocalDateTime created = LocalDateTime.now();

    public ItemRequestCreatedDto(String description) {
        this.description = description;
    }
}
