package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
@Data
public class MemoryUserRepository {
    private final Map<Long, User> users = new HashMap<>();
    @Setter(AccessLevel.PRIVATE)
    @Getter(AccessLevel.PRIVATE)
    private Long id = 0L;

    public void create(User user) {
        users.put(user.getId(), user);
    }

    public void update(User user) {
        users.put(user.getId(), user);
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public Optional<User> find(Long userId) {
        return users.values().stream().filter(el -> el.getId().equals(userId)).findFirst();
    }

    public void delete(Long userId) {
        users.remove(userId);
    }

    public Long nextId() {
        return ++id;
    }
}
