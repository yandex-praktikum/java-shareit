package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {
    private Integer id;

    @NotBlank(message = "name не может быть пустым")
    @Pattern(regexp = "\\S+", message = "name не может содержать пробелов")
    private String name;

    @NotBlank(message = "email не может быть пустым")
    @Email(message = "некорректный e-mail")
    private String email;
}
