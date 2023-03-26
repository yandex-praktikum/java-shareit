package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * утилитарный класс для преобразования User <--> UserDto
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDtoMapper {
    /**
     * преобразовать dto в бизнес-объект User
     *
     * @param userDto объект UserDto
     * @return объект User или null если на входе был null
     */
    public static User toUser(UserDto userDto) {
        if (userDto != null) {
            return User.builder()
                    .id(userDto.getId())
                    .name(userDto.getName())
                    .email(userDto.getEmail()).build();
        } else {
            return null;
        }
    }

    /**
     * преобразовать бизнес-объект User в dto
     *
     * @param user объект User
     * @return объект UserDto или null если на входе был null
     */
    public static UserDto toUserDto(User user) {
        if (user != null) {
            return UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail()).build();
        } else {
            return null;
        }
    }

    /**
     * преобразовать список бизнес-объектов User в список dto
     *
     * @param userList список объектов User
     * @return список объектов UserDto
     */
    public static List<UserDto> toUserDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();

        if (userList != null) {
            for (User user : userList) {
                userDtoList.add(toUserDto(user));
            }
        }

        return userDtoList;
    }
}