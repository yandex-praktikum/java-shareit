package ru.practicum.shareit.user;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapUser(UserDto userDto);

    UserDto mapUserDto(User user);
}
