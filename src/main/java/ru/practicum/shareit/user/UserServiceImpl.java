package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public User get(long userId) throws NotFoundException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("такого пользователя нет в списке"));
        return user;
    }

    public User add(User user) throws AlreadyExistException {
        if (user.getName() == null) {
            log.info("Имя пользователя отсутствует");
            throw new ValidationException("Нет имени пользователя");
        }
        if (user.getEmail() == null) {
            log.info("У пользователя отсутствует email");
            throw new ValidationException("Нет адреса почты пользователя");
        }
        userRepository.createUser(user);
        return user;
    }

    public User update(long userId, User user) {
        if (userId <= 0) {
            log.info("ID пользователя равен 0");
            throw new NullPointerException("ID пользователя равен 0");
        }
        return userRepository.updateUser(userId, user);
    }

    public Collection<User> getUsers() {
        return userRepository.getUsers();
    }

    public void remove(long userId) {
        if (userId <= 0) {
            log.info("ID пользователя равен 0");
            throw new NullPointerException("ID пользователя меньше или равно 0");
        }
        userRepository.removeUser(userId);
    }
}