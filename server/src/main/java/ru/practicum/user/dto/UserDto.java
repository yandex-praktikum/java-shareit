package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;


@Builder
@Data
@Jacksonized
public class UserDto {

    Long id;

    String name;

    @Email
    String email;
}
