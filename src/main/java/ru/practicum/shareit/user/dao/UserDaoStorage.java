package ru.practicum.shareit.user.dao;


import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDaoStorage {

    User addToUserMap(User user);

    Optional<User> update(Long id, User user);

    Optional<User> getUserById(Long id);

    List<User> getAll();

    void deleteUserById(Long id);

}
