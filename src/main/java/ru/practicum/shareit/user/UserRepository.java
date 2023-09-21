package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User add(User userToAdd);

    boolean containsEmail(String email);

    User update(UserDto userToUpdate, Long userId);

    User getUser(Long userId);

    List<User> getAll();

    void delete(Long userId);

    void containsById(Long userId);

    void containsEmailForUpdate(String email,Long userId);
}
