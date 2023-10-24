package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@EqualsAndHashCode
@ToString
@Component
public class User {

    private int id;
    @NotBlank(message = "Name - не может быть пустым.")
    private String name;
    @NotBlank(message = "Email - не может быть пустым.")
    @Email(message = "Не правильный формат email")
    private String email;
}
