package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO User - то что увидит пользователь
 */

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "Логин или имя не может быть пустым")
    private String name;

    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Укажите корректную почту")
    private String email;
}
