package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Map<Long, User> dataStorage = new HashMap<>();
    private Long sequenceId = 1L;

    @Override
    public void delete(Long id) {
        dataStorage.remove(id);
    }

    @Override
    public List<UserDto> findAll() {
        return dataStorage.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(Long id) {
        User user = dataStorage.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден: " + id);
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User existingUser = dataStorage.get(id);
        if (existingUser == null) {
            throw new NotFoundException("Невозможно обновить: пользователь " + id + " не существует");
        }

        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(existingUser.getEmail())) {
            if (!dto.getEmail().contains("@")) {
                throw new ValidationException("Некорректный формат email");
            }
            validateEmailUniqueness(dto.getEmail(), id);
            existingUser.setEmail(dto.getEmail());
        }

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            existingUser.setName(dto.getName());
        }

        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto create(UserDto dto) {
        validateEmailUniqueness(dto.getEmail(), null);
        User newUser = UserMapper.toUser(dto);
        newUser.setId(sequenceId++);
        dataStorage.put(newUser.getId(), newUser);
        return UserMapper.toUserDto(newUser);
    }

    private void validateEmailUniqueness(String email, Long currentUserId) {
        boolean isDuplicate = dataStorage.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email) && !u.getId().equals(currentUserId));
        if (isDuplicate) {
            throw new ConflictException("Электронная почта " + email + " уже используется");
        }
    }
}