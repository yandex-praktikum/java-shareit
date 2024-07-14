package ru.practicum.shareit.user.service.impl;


import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.user.exception.InvalidUserDataException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userStorage.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new InvalidUserDataException("Email cannot be empty");
        }
        User user = UserMapper.toEntity(userDto);
        user = userStorage.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            if (userStorage.existsByEmail(userDto.getEmail())) {
                throw new EmailAlreadyExistsException("Email already exists");
            }
            user.setEmail(userDto.getEmail());
        }
        user = userStorage.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = userStorage.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteById(userId);
    }
}