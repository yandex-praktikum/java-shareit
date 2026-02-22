package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAll() {
        log.info("Получение всех пользователей");
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        log.info("Получение пользователя с id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        log.info("Создание пользователя с email {}", userDto.getEmail());
        User user = UserMapper.toUser(userDto);
        User saved = userRepository.save(user);
        return UserMapper.toUserDto(saved);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        log.info("Обновление пользователя с id {}", id);
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (userDto.getName() != null) {
            existing.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existing.setEmail(userDto.getEmail());
        }

        User updated = userRepository.update(existing);
        return UserMapper.toUserDto(updated);
    }

    @Override
    public void delete(Long id) {
        log.info("Удаление пользователя с id {}", id);
        userRepository.delete(id);
    }
}