package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserMapper {

    public UserDto makeDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public User makeModel(UserDto userDto, Long userId) {
        return new User(userId, userDto.getName(), userDto.getEmail());
    }

    public List<UserDto> makeUserListToUserDtoList(List<User> users) {
        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(makeDto(user));
        }
        return result;
    }
}