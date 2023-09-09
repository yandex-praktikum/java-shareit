package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.UpdateGroupMarker;
import ru.practicum.shareit.user.UserCreateGroupMarker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotNull(groups = UpdateGroupMarker.UpdateMarker.class)
    long id;//— уникальный идентификатор пользователя;

    @NotBlank(groups = UserCreateGroupMarker.UserCreateMarker.class)
    String name; //— имя или логин пользователя;

    @NotNull(groups = UserCreateGroupMarker.UserCreateMarker.class)
    @Email
    String email;//— адрес электронной почты (учтите, что два пользователя не могут иметь одинаковый адрес электронной почты).

}
