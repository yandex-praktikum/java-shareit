package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
public class UserServiceInMemoryImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceInMemoryImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получен запрос на вывод всех пользователей");
        return repository.getAllUsers();
    }

    @Override
    public UserDto createUser(User user) {
        log.info("Получен запрос на создание пользователя");
        return repository.addUser(user);
    }

    @Override
    public UserDto changeUser(int userId, UserDto userDto) {
        log.info("Получен запрос на изменение пользователя");
        return repository.updateUser(userId, userDto);
    }

    @Override
    public UserDto findUserById(int id) {
        return UserMapper.userToUserDto(repository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Пользователь с id = " + id + " не найден")));
    }

    @Override
    public void removeUser(int id) {
        repository.deleteUser(id);
    }
}
