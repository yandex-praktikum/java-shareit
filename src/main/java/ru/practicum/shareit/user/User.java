package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@With
@Builder
public class User {

    long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name must be shorter than 100 characters")
    String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be valid")
    String email;
}
