package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.AlreadyExistException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class UserStorage implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();

    private long id;

    public User createUser(User user) throws AlreadyExistException {
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.info("Пользователь уже существует");
                throw new AlreadyExistException("Такой пользователь уже существует");
            }
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Пользователь создан успешно");
        return user;
    }

    @Override
    public Optional<User> findUserById(long userId) {
        try {
            return Optional.ofNullable(users.get(userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public User updateUser(long userId, User user) {
        User newUser = users.get(userId);
        if (user.getName() != null) {
            newUser.setName(user.getName());
        } else {
            for (User user1 : users.values()) {
                if (user1.getEmail().equals(user.getEmail())) {
                    log.info("Email уже существует");
                    throw new IllegalArgumentException("Email уже существует");
                }
            }
        }
        if (user.getEmail() != null) {
            newUser.setEmail(user.getEmail());
        }
        users.put(userId, newUser);
        log.info("Обновлены данные пользователя ID# " + userId);
        return users.get(userId);
    }

    @Override
    public void removeUser(long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public UserDto getUser(long userId) {
        return UserMapper.toUserDto(users.get(userId));
    }
}