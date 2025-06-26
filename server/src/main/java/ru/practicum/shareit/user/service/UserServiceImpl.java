package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers() {
        log.info("Запрос на получение информации о пользователях");
        return repository.findAll()
                .stream().map(mapper::mapUserDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getUserById(Long id) {
        log.info("Запрос на получение информации о пользователе id:" + id);
        User user =  repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не удалось нйти пользователя с id:" + id));
        return mapper.mapUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto user) {
        log.info("Создание пользователя");
        User userEntity = mapper.mapUser(user);
        return mapper.mapUserDto(repository.save(userEntity));
    }

    @Override
    public UserDto updateUser(UserDto user) {
        log.info("Обновление информации о пользователе " + user.getId());
        User oldUser = repository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Не удалось нйти пользователя с id:" + user.getId()));

        if (user.getEmail() != null && !oldUser.getEmail().equals(user.getEmail())) {
            log.trace("Изменение email пользователя");
            oldUser.setEmail(user.getEmail());
        }

        if (user.getName() != null && !oldUser.getName().equals(user.getName())) {
            log.trace("Изменение имя пользователя");
            oldUser.setName(user.getName());
        }

        return mapper.mapUserDto(repository.save(oldUser));
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Удаление пользователя " + id);
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не удалось нйти пользователя с id:" + id));
        repository.delete(user);
    }
}
