package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.error.ConflictException;
import ru.practicum.shareit.error.NotFoundException;
import ru.practicum.shareit.error.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> emailToUserMap = new HashMap<>(); // Исправлено: храним User вместо Long
    private Long idCounter = 1L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return users.get(userId);
    }

    @Override
    public User createUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный формат email");
        }

        // Проверяем, существует ли уже пользователь с таким email
        if (emailToUserMap.containsKey(user.getEmail())) {
            throw new ConflictException("Пользователь с email " + user.getEmail() + " уже существует");
        }

        user.setId(idCounter++);
        users.put(user.getId(), user);
        emailToUserMap.put(user.getEmail(), user); // Сохраняем пользователя
        return user;
    }

    @Override
    public User updateUser(Long userId, User userUpdates) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        User existingUser = users.get(userId);

        if (userUpdates.getEmail() != null) {
            String newEmail = userUpdates.getEmail();
            if (newEmail.isBlank() || !newEmail.contains("@")) {
                throw new ValidationException("Некорректный формат email");
            }

            // Проверяем, не занят ли новый email другим пользователем
            if (emailToUserMap.containsKey(newEmail) &&
                    !emailToUserMap.get(newEmail).getId().equals(userId)) {
                throw new ConflictException("Email " + newEmail + " уже используется другим пользователем");
            }

            // Обновляем email в мапе
            emailToUserMap.remove(existingUser.getEmail());
            existingUser.setEmail(newEmail);
            emailToUserMap.put(newEmail, existingUser);
        }

        if (userUpdates.getName() != null) {
            existingUser.setName(userUpdates.getName());
        }

        return existingUser;
    }

    @Override
    public void deleteUser(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        User user = users.get(userId);
        emailToUserMap.remove(user.getEmail()); // Удаляем email из мапы
        users.remove(userId);
    }
}