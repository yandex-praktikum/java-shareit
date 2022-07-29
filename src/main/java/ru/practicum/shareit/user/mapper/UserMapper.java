package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * Маппер между объектами Item и ItemDto
 */
@Mapper
public interface UserMapper {
    User toUser(UserDto dto);

    UserDto toDto(User user);
}
