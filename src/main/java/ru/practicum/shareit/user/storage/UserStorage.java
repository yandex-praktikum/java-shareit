package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;

import java.util.List;

public interface UserStorage {
    public UserDto addUser(User user);

    public UserDto updateUser(Long userId, User user);

    public List<UserDto> getUsers();

    public UserDto getUser(Long id);

    public void deleteUser (Long id);
    public List <String> getEmailList ();
    public List <Long> getUserId();
}
