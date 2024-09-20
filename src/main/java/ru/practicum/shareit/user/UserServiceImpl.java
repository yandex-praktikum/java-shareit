package ru.practicum.shareit.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictExceptions;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

@Data
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto addUser(User user) {
        if (userRepository.validationEmail(user) != 0) {
            throw new ConflictExceptions("Пользователь с email " + user.getEmail() + " существует");
        }
        return UserMapper.mapToUserDto(userRepository.addUser(user));
    }

    @Override
    public UserDto updateUser(long userId, User user) {
        if (!userRepository.validationUserId(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (userRepository.validationEmail(user) != 0) {
            throw new ConflictExceptions("Пользователь с email " + user.getEmail() + " существует");
        }
        return UserMapper.mapToUserDto(userRepository.updateUser(userId, user));
    }

    @Override
    public UserDto getUserDtoById(long userId) {
        if (!userRepository.validationUserId(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return UserMapper.mapToUserDto(userRepository.getUserById(userId));
    }

    @Override
    public void deleteUserById(long userId) {
        if (!userRepository.validationUserId(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        userRepository.deleteUserById(userId);
    }

    @Override
    public User getUserById(long userId) {
        if (!userRepository.validationUserId(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return userRepository.getUserById(userId);
    }
}
