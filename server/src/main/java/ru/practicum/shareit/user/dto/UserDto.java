package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}