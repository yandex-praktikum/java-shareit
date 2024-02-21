package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User {

    @Positive(message = "id пользователя должно быть больше 0.")
    private Integer id;

    @NotBlank(message = "Имя не может бвть пустым.")
    @Size(min = 4, max = 20, message = "Длина имени должна быть от 4 до 20 символов.")
    private String name;

    @NonNull
    @Email(message = "Поле должно содержать email.")
    private String email;

}
