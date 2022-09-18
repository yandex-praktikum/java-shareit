package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

interface UserRepository {
    List<User> findAll();

    User find(Long id);

    User add(User user);

    User update(Long id, User user);

    void delete(Long id);

}