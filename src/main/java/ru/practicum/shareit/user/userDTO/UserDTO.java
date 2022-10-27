package ru.practicum.shareit.user.userDTO;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
}