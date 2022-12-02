package ru.practicum.shareit.user.dto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDto userToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User userFromUserDto(UserDto userDto, Integer userId) {
        return new User(userId, userDto.getName(), userDto.getEmail());
    }
}
