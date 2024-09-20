package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.validation.Marker;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
public class User {
    Long id;
    @NotNull (groups = Marker.AddUser.class)
    String name;
    @NotNull (groups = Marker.AddUser.class)
    @Email
    String email;
}
