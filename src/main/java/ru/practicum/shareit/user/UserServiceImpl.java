package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadUserException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        if (userStorage.getUsers().stream()
                .anyMatch(u -> Objects.equals(u.getEmail(),
                        user.getEmail()))) {
            throw new BadUserException("User with the same email is already exists");
        }
        return userStorage.createUser(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id = " + id + " is not found");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User updateUser(User user) {
        User currUser = getUserById(user.getId());
        if (userStorage.getUsers().stream()
                .anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail()) && u.getId() != user.getId())) {
            throw new BadUserException("User with the same email is already exists");
        }
        if (user.getName() != null) {
            currUser = currUser.withName(user.getName());
        }
        if (user.getEmail() != null) {
            currUser = currUser.withEmail(user.getEmail());
        }
        return userStorage.updateUser(currUser);
    }

    @Override
    public void removeUserById(Long id) {
        if (userStorage.getUserById(id) == null) {
            throw new NotFoundException("User with id = " + id + " is not found");
        }
        userStorage.removeUserById(id);
    }
}
