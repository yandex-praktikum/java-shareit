package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {

    private Long id;

    @NotBlank(message = "Логин или имя не может быть пустым")
    private String name;

    @NotBlank(message = "Почта не должна быть пустой")
    @Email(message = "Укажите корректную почту")
    private String email;
}
