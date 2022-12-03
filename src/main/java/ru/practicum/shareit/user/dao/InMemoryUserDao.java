package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateDataException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserDao implements UserDao {

    private final Map<Long, User> usersDao;
    private long idGenerator = 0;

    @Override
    public User getUserById(Long usedId) {
        return usersDao.get(usedId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersDao.values());
    }

    @Override
    public User createUser(User user) {
        if (checkUserData(user)) {
            user.setId(++idGenerator);
            usersDao.put(user.getId(), user);
            return user;
        } else {
            throw new DuplicateDataException("Пользователь с таким email уже существует.");
        }
    }

    @Override
    public User updateUser(Long userId, User user) {
        User userForUpdate = usersDao.get(userId);
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            if (checkUserData(user)) {
                userForUpdate.setEmail(user.getEmail());
            } else {
                throw new DuplicateDataException("Не удалось обновить данные пользователя. "
                        + "Пользователь с таким email уже существует");
            }
        }
        return userForUpdate;
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (usersDao.containsKey(userId)) {
            usersDao.remove(userId);
            return true;
        } else {
            return false;
        }
    }

    private boolean checkUserData(User user) {
        boolean userCheck = usersDao.entrySet().stream()
                                    .anyMatch(t -> t.getValue().getEmail().equals(user.getEmail()));
        return !userCheck;
    }
}
