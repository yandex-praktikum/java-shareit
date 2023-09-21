package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class User {
    Long id;

    @NotEmpty
    String name;

    @Email
    @NotEmpty
    String email;
}
