package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.*;

@Repository
public class UserStorageImpl implements UserStorage {
    private long counter = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        userDto.setId(counter++);
        users.put(userDto.getId(), UserMapper.toUser(userDto));
        return userDto;
    }

    @Override
    public User update(Long id, User user) {
        if (users.containsKey(id)) {
            if (user.getName() != null) {
                users.get(id).setName(user.getName());
            }
            if (user.getEmail() != null) {
                users.get(id).setEmail(user.getEmail());
            }
                return users.get(id);
            } else  {
                throw new ObjectNotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }
}
