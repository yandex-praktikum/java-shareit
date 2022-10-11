package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    User getById(Integer id);

    Collection<User> getAll();

    User add(User user);

    User update(User user, Integer id);

    void delete(Integer id);

    void deleteAll();
}
