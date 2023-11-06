package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User create(User user);

    User update(User user);

    void delete(Long id);

    User findUserByEmail(String email);

    User findUserById(Long id);
}
