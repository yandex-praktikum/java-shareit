package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        //     user.setId(userDto.getId() != null ? userDto.getId() : null);     // //оставил на всякий случай
        return user;
    }


    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
//userDto.setId(user.getId() != null ? user.getId() : null);    // //оставил на всякий случай
        return userDto;
    }

    public static User mapToUserUpdate(long userId, UserDto userDto) {
        User user = new User();

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setId(userId);
        return user;
    }
}