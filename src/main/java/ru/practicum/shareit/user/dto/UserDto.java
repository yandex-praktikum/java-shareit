package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserDto extends User {
    private Long id;
    @NonNull
    private String name;
    @Email
    @NotBlank
    private String email;
}
