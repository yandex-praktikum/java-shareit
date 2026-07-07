package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String name;
    private String email;
    private String registrationDate;
}