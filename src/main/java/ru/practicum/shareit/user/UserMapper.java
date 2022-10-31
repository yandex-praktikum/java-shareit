package ru.practicum.shareit.user;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;

@Component
@Getter
public class UserMapper {
    private Long id = 0L;

    private Long generateId() {
        ++id;
        return getId();
    }

    public User fromUserDto(UserDto userDto) {
        return new User(generateId(), userDto.getName(), userDto.getEmail());
    }
}
