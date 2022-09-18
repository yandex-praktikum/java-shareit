package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;


public interface UserMapper {
    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);
}
