package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
@Mapper
public class UserMapperImpl implements UserMapper {

    public UserDto userToUserDto(User user) {
        return new UserDto(user.getName(), user.getEmail());
    }

    public User userDtoToUser(UserDto userDto) {
        return new User(null, userDto.getName(), userDto.getEmail());
    }

}
