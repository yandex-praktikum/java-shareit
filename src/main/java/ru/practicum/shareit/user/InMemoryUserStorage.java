package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private static int generateId = 1;

    private final HashMap<Integer, User> users = new HashMap<>();

    private static Integer getNextId() {
        return generateId++;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.userFromUserDto(userDto, getNextId());
        users.put(user.getId(), user);
        return UserMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUser(Integer userId, UserDto userDto) {
        User user = UserMapper.userFromUserDto(userDto, userId);
        users.put(user.getId(), user);
        return UserMapper.userToUserDto(user);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return users.values().stream().map(UserMapper::userToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto findUserDtoById(Integer userId) {
        return UserMapper.userToUserDto(users.get(userId));
    }

    @Override
    public User findUserById(Integer userId) {
        return users.get(userId);
    }

    @Override
    public void deleteUserById(int userId) {
        users.remove(userId);
    }
}
