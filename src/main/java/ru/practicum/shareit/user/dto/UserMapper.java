package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public final class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

}
