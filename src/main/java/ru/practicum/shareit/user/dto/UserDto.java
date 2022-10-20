package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;

    public User toUser() {
        return new User(
                this.getId(),
                this.getName(),
                this.getEmail()
        );
    }

}
