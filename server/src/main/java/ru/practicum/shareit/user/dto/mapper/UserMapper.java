package ru.practicum.shareit.user.dto.mapper;

import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static Collection<UserDto> toUserDto(Collection<User> users) {
        return users.stream().map(UserMapper::toUserDto).toList();
    }

    public static User fromDto(RequestUserDto requestUserDto) {
        return new User(null, requestUserDto.getName(), requestUserDto.getEmail());
    }

    public static User fromDto(Long userId, RequestUserDto requestUserDto) {
        return new User(userId, requestUserDto.getName(), requestUserDto.getEmail());
    }
}
