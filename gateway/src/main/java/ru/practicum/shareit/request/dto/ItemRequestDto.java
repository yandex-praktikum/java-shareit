package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotBlank(message = "Описание запроса не может быть пустым")
    private String description;

    @Positive(message = "ID пользователя должен быть положительным числом")
    private Long requestorId;

    private LocalDateTime created;
    private List<ItemInfo> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemInfo {
        @Positive(message = "ID вещи должен быть положительным числом")
        private Long id;

        private String name;

        @Positive(message = "ID владельца должен быть положительным числом")
        private Long ownerId;
    }
}