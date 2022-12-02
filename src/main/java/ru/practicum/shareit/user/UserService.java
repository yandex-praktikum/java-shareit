package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return UserMapper.toUserDto(user);
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

    public User updateUser(UserDto userDto) {
        log.info("Обновление пользователя {}", userDto);
        return userDao.updateUser(UserMapper.toUser(userDto));
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
