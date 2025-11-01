package ru.practicum.shareit.user;

import java.util.Collection;

public interface UserService {

    //   получения всех пользователей
    Collection<UserDto> getAllUsers();

    //  создания нового пользователя
    UserDto createUser(UserDto userDto);

    // Обновить информацию о пользователе по id
    UserDto updateUser(Long userId, UserDto updates);

    //Найти пользователя по id
    User findUserById(Long userId);

    //удаление пользователя по id
    void deleteUser(Long id);
}
