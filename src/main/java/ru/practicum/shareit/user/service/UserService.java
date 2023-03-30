package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RestController
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("InMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto addUser(User user) throws ValidationException, BadRequestException {
        if (user.getEmail() == null) {
            throw new BadRequestException("Email не может быть пустым");
        }
        checkEmail(user);
        return userStorage.addUser(user);
    }

    public UserDto updateUser(Long userId, User user) throws ValidationException {
        if (userStorage.getUser(userId).getEmail().equals(user.getEmail())) {
            return userStorage.getUser(userId);
        }
        checkEmail(user);
        return userStorage.updateUser(userId, user);
    }

    public UserDto getUser(Long id) {
        return userStorage.getUser(id);
    }

    public List<UserDto> getUsers() {
        return userStorage.getUsers();
    }

    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    private void checkEmail(User user) throws ValidationException {
        if (userStorage.getEmailList().contains(user.getEmail())) {
            throw new ValidationException("Такой email уже используется");
        }
    }
}
