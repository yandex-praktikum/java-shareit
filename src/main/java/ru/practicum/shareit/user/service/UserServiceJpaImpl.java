package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.repository.UserJpaRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceJpaImpl implements UserService {

    private final UserJpaRepository repository;

    @Override
    public List<User> getAllUsers() {
        log.info("Получен запрос на вывод всех пользователей");
        return repository.findAll();
    }

    @Override
    public UserDto createUser(User user) {
        log.info("Получен запрос на создание пользователя");
        if ((user.getName() != null) && (user.getEmail() != null)) {
            try {
                return UserMapper.userToUserDto(repository.save(user));
            } catch (RuntimeException exception) {
                log.info("Ошибка создания пользователя");
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Некорректный запрос");
            }
        } else {
            log.info("Ошибка валидации пользователя");
            throw new ValidationException();
        }
    }

    @Override
    public UserDto changeUser(int userId, UserDto userDto) {
        log.info("Получен запрос на изменение пользователя");
        User updatedUser;
        if (repository.findById(userId).isPresent()) {
            User user = repository.findById(userId).get();
            if (userDto.getName() == null) {
                updatedUser = updateEmail(user, userDto);
            } else if (userDto.getEmail() == null) {
                updatedUser = updateName(user, userDto);
            } else {
                updatedUser = updateNameAndEmail(user, userDto);
            }
            log.info("Пользовател обновлен");
            return UserMapper.userToUserDto(repository.save(updatedUser));
        } else {
            log.info("Пользователь с id = " + userId + " не найден");
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserDto findUserById(int id) {
        return UserMapper.userToUserDto(repository.findById(id).orElseThrow(() ->
                new UserNotFoundException("Пользователь с id = " + id + " не найден")));
    }

    @Override
    public void removeUser(int id) {
        repository.deleteById(id);
    }

    private User updateEmail(User user, UserDto userDto) {
        return User.builder()
                .id(user.getId())
                .email(userDto.getEmail())
                .name(user.getName())
                .build();
    }

    private User updateName(User user, UserDto userDto) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(userDto.getName())
                .build();
    }

    private User updateNameAndEmail(User user, UserDto userDto) {
        return User.builder()
                .id(user.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

}