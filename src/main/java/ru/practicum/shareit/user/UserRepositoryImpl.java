package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    private static long generatorId = 0;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        user.setId(++generatorId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public void removeUser(Long userId) {
        users.remove(userId);
    }

    @Override
    public User updateUser(UserDto userDto, Long userId) {
        if (userDto.getName() != null) {
            users.get(userId).setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            users.get(userId).setEmail(userDto.getEmail());
        }
        return users.get(userId);
    }
}
