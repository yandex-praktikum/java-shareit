package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserDto {
    //  private Long id;   //оставил на всякий случай

    private String name;

    private String email;
}
