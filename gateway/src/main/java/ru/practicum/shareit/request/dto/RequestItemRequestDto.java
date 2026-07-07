package ru.practicum.shareit.request.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestItemRequestDto {
    @NotBlank(groups = OnCreate.class, message = "Описание вещи не может быть пустым.")
    @Size(min = 1, max = 1000)
    private String description;

    public interface OnCreate {}
}