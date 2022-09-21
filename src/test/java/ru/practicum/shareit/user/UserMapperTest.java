package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.user.UserTestData.user1;
import static ru.practicum.shareit.user.UserTestData.userDto1;

public class UserMapperTest {
    @Test
    public void toUserDto() {
        UserDto userDto = UserMapper.toUserDto(user1);
        assertThat(userDto, equalTo(userDto1));
    }

    @Test
    public void toUser() {
        User user = UserMapper.toUser(userDto1);
        assertThat(user, equalTo(user1));
    }
}
