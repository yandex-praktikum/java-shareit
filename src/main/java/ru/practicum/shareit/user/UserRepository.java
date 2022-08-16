package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.AlreadyExistException;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findUserById(long userId);

    User createUser(User user) throws AlreadyExistException;

    User updateUser(long userId, User user);

    void removeUser(long userId);

    List<User> getUsers();
}