package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {
//    public static UserDto toUserDto(User user) {
//        return UserDto.builder()
//                .name(user.getName())
//                .email(user.getEmail())
//                .build();
//    }
    public static User fromUserDto(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
