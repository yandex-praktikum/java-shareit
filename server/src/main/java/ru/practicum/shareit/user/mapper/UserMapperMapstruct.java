package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface UserMapperMapstruct {

    @Mapping(target = "registrationDate",
            source = "registrationDate",
            qualifiedByName = "instantToString")
    UserDto mapToDto(User user);

    User mapToUser(UserDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDto dto, @MappingTarget User user);

    @Named("instantToString")
    default String formatData(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy.MM.dd, hh:mm:ss")
                .withZone(ZoneId.of("UTC"));

        return formatter.format(instant);
    }
}