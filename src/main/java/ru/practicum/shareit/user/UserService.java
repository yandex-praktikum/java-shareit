package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    public List<UserDto> getAllUsers();

    public UserDto getUserById(long userId);

    public UserDto addNewUser(UserDto user);

    public UserDto changeUser(long userId, UserDto user);

    public void deleteUser(long userId);
}
