package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * Mapstruct хватит, чтобы на основании интерфейса UserMapper на этапе компиляции
 * сгенерировать нужную реализацию, которая будет переводить.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapToModel(UserDto itemDto);

    UserDto mapToDto(User user);
}
