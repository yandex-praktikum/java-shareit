package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;


public interface UserRepository {

    User save(User user);

    User update(User user);

    User findById(long id);

    Collection<User> findAll();

    void delete(long id);

    Optional<User> findByEmail(String email);

}
