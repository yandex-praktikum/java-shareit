package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String name;
    @Email
    private String email;

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(), user.getEmail()
        );
    }
}
