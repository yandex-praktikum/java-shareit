package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.AlreadyExistException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.Collection;

public interface UserService {

    User get(long userId) throws NotFoundException;

    User add(User user) throws AlreadyExistException;

    User update(long userId, User user);

    Collection<User> getUsers();

    void remove(long userId);
}