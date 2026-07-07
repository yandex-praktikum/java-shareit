package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID: " + userId + " не найден."));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long userId, User newUserData) {
        User oldUser = getUserById(userId);

        if (newUserData.getName() != null) {
            oldUser.setName(newUserData.getName());
        }
        if (newUserData.getEmail() != null) {
            oldUser.setEmail(newUserData.getEmail());
        }

        return repository.save(oldUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        getUserById(userId);
        repository.deleteById(userId);
    }
}