package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.userDTO.UserDTO;

public class UserMapper {
    public static UserDTO toUserDTO (User user){
         return UserDTO.builder()
             .id(user.getId())
                 .name(user.getName())
                 .email(user.getEmail())
                 .build();
    }

    public static User toUser (UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .build();
    }
}
