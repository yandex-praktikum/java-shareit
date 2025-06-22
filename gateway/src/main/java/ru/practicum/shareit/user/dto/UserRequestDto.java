package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
