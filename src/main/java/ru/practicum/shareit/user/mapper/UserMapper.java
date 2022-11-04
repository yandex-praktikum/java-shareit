package ru.practicum.shareit.user.mapper;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
@Getter
public class UserMapper {
    private Long id = 0L;

    private Long generateId() {
        ++id;
        return getId();
    }

    public User fromUserDtoCreate(UserDto userDto) {
        return new User(generateId(), userDto.getName(), userDto.getEmail());
    }

    public User fromUserDtoUpdate(UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }

    public UserDto fromUser(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

}
