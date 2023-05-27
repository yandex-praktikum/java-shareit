package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;

    private String name;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @NotEmpty
    @Email(message = "Электронная почта должна содержать символ @")
    private String email;
}
