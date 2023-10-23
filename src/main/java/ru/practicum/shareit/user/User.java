package ru.practicum.shareit.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;

@Data
@EqualsAndHashCode
@ToString
public class User {

    private int id;
    private String name;
    @Email(message = "Не правильный формат email")
    private String email;
}
