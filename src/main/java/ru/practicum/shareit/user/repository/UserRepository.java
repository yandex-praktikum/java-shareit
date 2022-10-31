package ru.practicum.shareit.user.repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User getById(Long id);

    User create(User user);

    User update(User user);

    void delete(Long id);
}
