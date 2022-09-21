package ru.practicum.shareit.user;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.NullEmailException;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new NullEmailException("Емейл не может быть null");
        }
        return new User(userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}
