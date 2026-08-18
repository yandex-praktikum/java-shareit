package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ParameterNotValidException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {

        log.info("Создание пользователя с email={}", userDto.getEmail());

        String email = userDto.getEmail();

        if (email == null || email.isBlank()) {
            throw new ParameterNotValidException("Ваш email не может быть пустым");
        }

        if (!email.contains("@")) {
            throw new ParameterNotValidException("Неправильный ввод почты, она должна содрежать знак @");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Данный email: " + email + " уже зарегистрирован");
        }

        User user = UserMapper.toUser(userDto);

        log.info("Пользователь {} успешно создан", user.getEmail());

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {

        log.info("Обновление пользователя {}", userId);

        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден")
        );

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {

            boolean exists = userRepository.findAll().stream()
                    .anyMatch(user ->
                            user.getId() != userId &&
                                    user.getEmail().equals(userDto.getEmail()));

            if (exists) {
                throw new DuplicateEmailException("Данный email уже зарегестрирован");
            }

            existingUser.setEmail(userDto.getEmail());
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("Пользователь {} успешно обновлен", userId);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public UserDto getById(long userId) {

        log.info("Получение пользователя {}", userId);

        User user = userRepository.getById(userId);

        if (user == null) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }

        return UserMapper.toUserDto(user);
    }

    @Override
    public Collection<UserDto> getAllUsers() {

        log.info("Получение списка всех пользователей");

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public void delete(long userId) {

        log.info("Удаление пользователя {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        userRepository.deleteById(userId);
        log.info("Пользователь {} успешно удален", userId);
    }
}
