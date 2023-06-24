package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.EmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage{

    public static final String USER_NOT_FOUND = "Пользователь с id не найден : ";
    public static final String EMAIL_ERROR = "Пользователь с email уже существует :";

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    private long currentId = 1;

    @Override
    public User createUser(User user) {
        checkEmailIsAlreadyUsed(user.getEmail());
        user.setId(generateId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(long userId, User user) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException(USER_NOT_FOUND + userId);
        }
        User oldUser = users.get(userId);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        String oldEmail = users.get(userId).getEmail();
        String newEmail = user.getEmail();
        if (newEmail != null && !oldEmail.equals(newEmail)) {
            tryRefreshUserEmail(oldEmail, newEmail);
            oldUser.setEmail(newEmail);
        }
        return oldUser;
    }

    @Override
    public User getUserById(long userId) {
        if (!users.containsKey(userId)) throw new UserNotFoundException(USER_NOT_FOUND + userId);
        return users.get(userId);
    }

    @Override
    public void deleteUserById(long userId) {
        if (!users.containsKey(userId)) throw new UserNotFoundException(USER_NOT_FOUND + userId);
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void checkEmailIsAlreadyUsed(String email) {
        if (emails.contains(email)) throw new EmailException(EMAIL_ERROR + email);
    }

    private void tryRefreshUserEmail(String oldEmail, String newEmail) {
        emails.remove(oldEmail);
        if (emails.contains(newEmail)) {
            emails.add(oldEmail);
            throw new EmailException(EMAIL_ERROR + newEmail);
        }
        emails.add(newEmail);
    }

    private long generateId() {
        return currentId++;
    }
}
