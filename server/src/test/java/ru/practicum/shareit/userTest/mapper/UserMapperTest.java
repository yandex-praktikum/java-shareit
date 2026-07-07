package ru.practicum.shareit.userTest.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.mapper.UserMapperMapstruct;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    private UserMapperMapstruct userMapperMapstruct;

    @BeforeEach
    public void setUp() {
        userMapperMapstruct = Mappers.getMapper(UserMapperMapstruct.class);
    }

    @Test
    public void mapToDtoShouldCopyFieldsWhenUserIsValid() {
        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@ya.ru");

        UserDto dto = userMapperMapstruct.mapToDto(user);

        assertNotNull(dto);
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }

    @Test
    public void mapToUserShouldCopyFieldsWhenUserIsValid() {
        UserDto dto = new UserDto();
        dto.setName("Вася");
        dto.setEmail("vasya@ya.ru");

        User user = userMapperMapstruct.mapToUser(dto);

        assertNotNull(dto);
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
        assertNull(user.getId());
    }

    @Test
    public void updateUserFromDtoTest() {
        UserDto dto = new UserDto();
        dto.setName("Петя");
        dto.setEmail("petya@ya.ru");

        User user = new User();
        user.setId(1L);
        user.setName("Вася");
        user.setEmail("vasya@ya.ru");

        userMapperMapstruct.updateUserFromDto(dto, user);

        assertEquals("Петя", user.getName());
        assertEquals("petya@ya.ru", user.getEmail());
        assertEquals(1L, user.getId());
    }
}