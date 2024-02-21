package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User add(User user);

    User update(UserDto user, int userId);

    void delete(int userId);

    User get(int userId);

//    User getByEmail(String email);

    List<User> getAll();


}
