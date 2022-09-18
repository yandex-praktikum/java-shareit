package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Long, User> users;
    private Long lastId;

    public UserRepositoryImpl() {
        this.users = new HashMap<>();
        this.lastId = Long.valueOf(0);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User find(Long id) {
        return users.get(id);
    }

    @Override
    public User add(User user) {
        user.setId(++lastId);
        users.put(lastId, user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        users.put(id, user);
        return user;
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

}
