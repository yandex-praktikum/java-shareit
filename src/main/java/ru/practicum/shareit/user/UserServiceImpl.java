package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemInMemoryRepository;
import ru.practicum.shareit.user.dao.UserInMemoryRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final ItemInMemoryRepository itemInMemoryRepository;
    private final UserInMemoryRepository userInMemoryRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("UserServiceImpl: Запрос на получение всех пользователей");
        return userInMemoryRepository.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long userId) {
        log.info("UserServiceImpl: Запрос на получение пользователя с ID={}", userId);
        Optional<User> user = userInMemoryRepository.getUserById(userId);
        return user.map(UserMapper::toUserDto).orElse(null);
    }

    @Override
    public UserDto addNewUser(UserDto user) {
        log.info("UserServiceImpl: Запрос на добавление пользователя {}", user);
        if (validation(UserMapper.toUser(user))) {
            User newUser = userInMemoryRepository.addNewUser(UserMapper.toUser(user));
            return UserMapper.toUserDto(newUser);
        }
        return null;
    }

    @Override
    public UserDto changeUser(long userId, UserDto user) {
        log.info("UserServiceImpl: Запрос на обновление данных пользователя {}", user);
        //Сначала проверим, что нет другого пользователя с таким же email
        List<UserDto> listOfUsers = getAllUsers();
        for (UserDto userToCheck : listOfUsers) {
            if (userToCheck.getEmail().equals(user.getEmail())) {
                if (!userToCheck.getName().equals(user.getName())) {
                    throw new ValidationException("Существует другой пользователь стаким email");
                }
            }
        }
        User newUser = userInMemoryRepository.changeUser(userId, UserMapper.toUser(user));
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public void deleteUser(long userId) {
        log.info("UserServiceImpl: Запрос на удаление пользователя с ID={}", userId);
        //Сначала удаляем все вещи пользователя
        itemInMemoryRepository.deleteAllUserItems(userId);
        //Потом удаляем самого пользователя
        userInMemoryRepository.deleteUser(userId);
    }

    private boolean validation(User user) {
        boolean result = true;
        if (user.getName().isEmpty()) {
            result = false;
            throw new ValidationException("Имя пользователя не может быть пустым");
        }
        if (user.getEmail().isEmpty()) {
            result = false;
            throw new ValidationException("Email пользователя не может быть пустым");
        }
        if (!user.getEmail().contains("@")) {
            result = false;
            throw new ValidationException("Неверный формат почтового адреса");
        }
        return result;
    }
}
