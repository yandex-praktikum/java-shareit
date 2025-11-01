package ru.practicum.shareit.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final Map<Long, User> users = new HashMap<>();

    //вывод всех пользователей
    @Override
    public Collection<UserDto> getAllUsers() {
        Collection<UserDto> userDtos = new ArrayList<>();

        for (User user : users.values()) {
            userDtos.add(UserMapper.toUserDto(user));
        }
        return userDtos;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        log.info("СЕРВИС - Попытка создания пользователя: {}", userDto);

        User user = UserMapper.toUser(userDto);
        try {
            // Проверка на существование пользователя с таким email
            for (User existingUser : users.values()) {
                if (existingUser.getEmail().equals(user.getEmail())) {
                    log.error("Пользователь с email {} уже существует", user.getEmail());
                    throw new ConflictException("Пользователь с таким email уже существует");
                }
            }
            user.setId(getNextId());
            users.put(user.getId(), user);
            log.info("Пользователь успешно создан: {}", user);
            return UserMapper.toUserDto(user);

        } catch (ValidationException e) {
            log.error("Создание пользователя завершилось ошибкой: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Произошла непредвиденная ошибка", e);
            throw new RuntimeException("Произошла непредвиденная ошибка", e);
        }
    }

    //обновление данных у пользователя
    @Override
    public UserDto updateUser(Long userId, UserDto updates) {
        User existingUser = users.get(userId);
        boolean wasUpdated = false;

        if (updates.getName() != null && !existingUser.getName().equals(updates.getName())) {
            existingUser.setName(updates.getName());
            log.info("Имя пользователя изменено на: {}", updates.getName());
            wasUpdated = true;
        }

        if (updates.getEmail() != null && !existingUser.getEmail().equals(updates.getEmail())) {
            for (User user : users.values()) {
                if (!user.getId().equals(userId) && user.getEmail().equals(updates.getEmail())) {
                    log.error("Email: {} уже используется пользователем {}", updates.getEmail(), user.getId());
                    throw new ConflictException("Email " + updates.getEmail() + " уже используется");
                }
            }
            existingUser.setEmail(updates.getEmail());
            log.info("Email пользователя изменен на: {}", updates.getEmail());
            wasUpdated = true;
        }

        if (!wasUpdated) {
            log.error("Редактирование пользователя завершилось ошибкой: данные не изменились");
            throw new ValidationException("Данные пользователя не изменились");
        }
        return UserMapper.toUserDto(existingUser);
    }

    //поиск пользователя по id
    @Override
    public User findUserById(Long userId) {

        log.info("Поиск пользователя с id = {}", userId);

        if (!users.containsKey(userId)) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return users.get(userId);
    }

    //удаление пользователя по id
    @Override
    public void deleteUser(Long userId) {
        findUserById(userId);
        users.remove(userId);
        log.info("Пользователь с ID {} удален", userId);
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
