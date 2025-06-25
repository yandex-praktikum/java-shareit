package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Repository

public class UserRepositoryInMemory implements UserRepository {

    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public User save(User user) {
        if (user.getId() != null) {
            log.warn("нельзя создать пользователя с id " + user.getId());
            throw new ValidationException("нельзя создать пользователя с id " + user.getId());
        }
        user.setId(getNextId());
        userMap.put(user.getId(), user);
        log.info("создан user c id " + user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        User oldUser = userMap.get(user.getId());
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        log.info("запись пользователя обновлена id " + oldUser.getId());
        return oldUser;
    }

    @Override
    public User findById(long id) {
        log.info("запись пользователя возвращена id " + id);
        return userMap.get(id);
    }

    @Override
    public Collection<User> findAll() {
        log.info("возвращен списов всех пользователе");
        return userMap.values();
    }

    @Override
    public void delete(long id) {
        log.info("пользвоателе удален id " + id);
        userMap.remove(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("поиск пользователя по email " + email);
        Optional<User> findUser = userMap.values().stream()
                .filter(user -> user.getEmail().equals(email)).findFirst();
        return findUser;
    }

    private long getNextId() {
        long currentMaxId = userMap.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
