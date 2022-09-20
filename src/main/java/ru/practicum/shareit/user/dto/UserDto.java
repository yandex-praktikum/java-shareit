package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private String name;
    @Email
    private String email;

}
