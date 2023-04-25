package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userStorage;

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(long userId, User user) {
        return userStorage.update(userId, user);
    }

    @Override
    public User findById(long userId) {
        return userStorage.findById(userId);
    }

    @Override
    public void delete(long userId) {
        userStorage.delete(userId);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }
}