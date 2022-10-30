package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


import javax.validation.constraints.NotBlank;

@ToString
@Getter
@AllArgsConstructor
public class UserNameDto {
    @NotBlank
    private String name;
}
