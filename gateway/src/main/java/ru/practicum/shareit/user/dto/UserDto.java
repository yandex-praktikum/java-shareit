package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    @NotBlank(groups = OnCreate.class, message = "Имя не может быть пустым")
    @Size(min = 1, max = 100, groups = {OnCreate.class, OnUpdate.class},
            message = "Имя должно быть от 1 до 100 символов")
    private String name;

    @NotBlank(groups = OnCreate.class, message = "Email не может быть пустым")
    @Email(groups = {OnCreate.class, OnUpdate.class},
            message = "Некорректный формат email")
    @Size(max = 255, groups = {OnCreate.class, OnUpdate.class},
            message = "Email должен быть не более 255 символов")
    private String email;

    private String registrationDate;

    public interface OnCreate {}

    public interface OnUpdate {}
}