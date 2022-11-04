package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ToString
@NonNull
@Getter
@AllArgsConstructor
public class UserDto {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
