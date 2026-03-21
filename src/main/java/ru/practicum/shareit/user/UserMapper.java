package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static User toUser(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .id(dto.getId())
                .build();
    }

    public static UserDto toUserDto(User model) {
        return UserDto.builder()
                .name(model.getName())
                .email(model.getEmail())
                .id(model.getId())
                .build();
    }
}