package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    User findById(Long userId);

    List<User> findAll();

    void deleteById(Long userId);

    boolean existsByEmail(String email);
}