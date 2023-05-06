package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotUniqueEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> getAllUsers() {
        return repository.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        User user = repository.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto saveNewUser(UserDto userDto) {
        validateUniqueEmail(userDto);
        User user = repository.saveNewUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        User user = repository.getUserById(userId).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        String name = userDto.getName();
        String email = userDto.getEmail();
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        if (email != null && !email.isBlank()) {
            if (!user.getEmail().equals(userDto.getEmail())) {
                validateUniqueEmail(userDto);
            }
            user.setEmail(email);
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(long id) {
        repository.deleteUser(id);
    }

    private void validateUniqueEmail(UserDto userDto) {
        if (repository.getAllUsers().stream().anyMatch(user -> user.getEmail().equals(userDto.getEmail()))) {
            throw new NotUniqueEmailException(String.format("Email %s уже используется.", userDto.getEmail()));
        }
    }
}
