package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final InMemoryUserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        User createdUser = userStorage.create(user);
        return UserMapper.toUserDto(createdUser);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }

        User updatedUser = userStorage.update(existingUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.delete(id);
    }
}
