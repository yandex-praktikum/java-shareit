package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, User> storage = new HashMap<>();

    @Override
    public User save(User user) {
        log.info("UserRepository: save({})", user);
        user.setId(counter.incrementAndGet());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("UserRepository: update({})", user);
        return storage.computeIfPresent(user.getId(), (id, oldU) -> user);
    }

    @Override
    public boolean delete(int userId) {
        log.info("UserRepository: delete({})", userId);
        return storage.remove(userId) != null;
    }

    @Override
    public User get(int userId) {
        log.info("UserRepository: get({})", userId);
        return storage.get(userId);
    }

    @Override
    public User getByEmail(String email) {
        log.info("UserRepository: getByEmail({})", email);
        return storage.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);

    }

    @Override
    public List<User> getAll() {
        log.info("UserRepository: getAll()");
        return new ArrayList<>(storage.values());
    }
}
