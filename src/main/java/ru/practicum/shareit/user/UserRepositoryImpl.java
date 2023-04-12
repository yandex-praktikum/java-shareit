package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final Map<Long, User> usersMap = new HashMap<>();
    private Long currentId = 1L;

    @Override
    public List<UserDto> getUsers() {
        return null;
    }

    @Override
    public User create(User user) throws UserAlreadyExistsException {
        if (usersMap.values().stream().noneMatch(u -> u.getEmail().equals(user.getEmail()))) {
                if (user.getId() == null) {
                    user.setId(++currentId);
                }
                usersMap.put(user.getId(), user);
        } else {
            throw new UserAlreadyExistsException("Пользователь с E-mail=" + user.getEmail() + " уже существует!");
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
