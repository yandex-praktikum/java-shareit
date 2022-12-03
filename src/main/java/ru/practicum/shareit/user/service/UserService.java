package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserDao userDao;

    public UserDto getUserById(Long userId) {
        log.info("Получение пользователя по ID = {}", userId);
        User user = userDao.getUserById(userId);
        if (user != null) {
            return UserMapper.toUserDto(user);
        } else {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден");
        }
    }

    public List<UserDto> getAllUsers() {
        log.info("Получение всех пользователей");
        List<User> users = userDao.getAllUsers();
        return users.stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto userDto) {
        log.info("Создание нового пользователя {}", userDto);
        User user = userDao.createUser(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Обновление пользователя с ID = {}", userId);
        User userForUpdate = userDao.updateUser(userId, UserMapper.toUser(userDto));
        return UserMapper.toUserDto(userForUpdate);
    }

    public boolean deleteUser(Long userId) {
        if (userDao.deleteUser(userId)) {
            log.info("Удаление пользователя ID = {} успешно", userId);
            return true;
        } else {
            log.info("Удаление пользователя ID = {} не удалось, пользователь не найден", userId);
            return false;
        }
    }
}
