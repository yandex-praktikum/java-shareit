package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserTestData {
    public static final UserDto userDto1 =
            UserDto.builder().id(1L).name("user1").email("user1@mail.ru").build();
    public static final UserDto userDto2 =
            UserDto.builder().id(2L).name("user2").email("user2@mail.ru").build();
    public static final UserDto userDto3 =
            UserDto.builder().id(3L).name("user3").email("user3@mail.ru").build();
    public static final UserDto userDtoCreated =
            UserDto.builder().id(4L).name("userCreated").email("userCreated@mail.ru").build();

    public static final User user1 = new User(1L, "user1", "user1@mail.ru");
    public static final User user2 = new User(2L, "user2", "user2@mail.ru");
}