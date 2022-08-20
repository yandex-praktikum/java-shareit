package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validator.Marker.*;
import ru.practicum.shareit.validator.NullOrNotBlank;

import javax.validation.constraints.*;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotBlank(groups = OnCreate.class, message = "The name should not be null or blank.")
    @NullOrNotBlank(groups = OnUpdate.class, message = "The name should be null or not blank.")
    String name;
    @Email
    @NotBlank(groups = OnCreate.class, message = "The email should not be null or blank.")
    @NullOrNotBlank(groups = OnUpdate.class, message = "The email should be null or not blank.")
    String email;

}
