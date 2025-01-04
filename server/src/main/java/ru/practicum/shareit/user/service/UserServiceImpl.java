package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.base.service.BaseInDbService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserEmailIsNotUnique;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
public class UserServiceImpl extends BaseInDbService<User, UserRepository> implements UserService {

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        super(repository, User.class);
    }

    @Override
    public UserDto create(RequestUserDto userDto) {
        User user = UserMapper.fromDto(userDto);
        checkingValidationEmail(user);
        User savedUser = super.save(user);
        log.info("User{id={}} успешно сохранен", savedUser.getId());
        return this.toDto(savedUser);
    }

    @Override
    public UserDto update(Long userId, RequestUserDto userDto) {
        User user = UserMapper.fromDto(userId, userDto);
        User storageUser = super.findById(user.getId());

        if (user.getName() != null && !user.getName().isBlank())
            storageUser.setName(user.getName());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            checkingValidationEmail(user);
            storageUser.setEmail(user.getEmail());
        }

        super.save(storageUser);
        log.info("User{id={}} успешно обновлен", storageUser.getId());

        return this.toDto(storageUser);
    }

    @Override
    public Optional<UserDto> findByEmail(String email) {
        Optional<User> anyUser = repository.findByEmail(email);
        return anyUser.map(this::toDto);
    }

    @Override
    public UserDto findUserById(Long id) {
        return this.toDto(super.findById(id));
    }

    @Override
    public void checkExistsById(Long id) {
        if (!super.repository.existsById(id))
            throw new NotFoundException(format("User{id=%d} не существует", id));
    }

    @Override
    public boolean existsById(Long id) {
        return super.repository.existsById(id);
    }


    @Override
    public Collection<UserDto> getAll() {
        return this.toDto(super.findAll());
    }

    private void checkingValidationEmail(User user) {
        if (this.findByEmail(user.getEmail()).isPresent())
            throw new UserEmailIsNotUnique("Пользователь с таким email уже существует");
    }

    private UserDto toDto(User user) {
        return UserMapper.toUserDto(user);
    }

    private Collection<UserDto> toDto(Collection<User> users) {
        return UserMapper.toUserDto(users);
    }
}
