package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        log.info("Получение всех пользователей");
        List<User> users = userRepository.findAll();
        return UserMapper.mapToItemDto(users);
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        log.info("Добавление пользователя");
        User user = UserMapper.fromUserDto(userDto);
        if (!(user.getEmail().contains("@"))) {
            throw new ValidationException("Email: " + userDto.getEmail() + "некорректный");
        } else {
            User newuser = userRepository.save(user);
            return UserMapper.toUserDto(newuser);
        }
    }

    @Override
    @Transactional
    public UserDto update(UserUpdateDto userUpdateDto, Long userId) {
        log.info("Обновление пользователя");
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new NotFoundException("Пользователь с id " + userId + " не найден")
                );

        if (userUpdateDto.getName() != null) {
                user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        userRepository.saveAndFlush(user);
        return UserMapper.toUserDto(user);

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Удаление пользователя");
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("Пользователь с Id " + id + " не найден");
        }
    }

    @Override
    public User getUserByIdOrThrow(Long userId) {
        log.info("Поиск пользователя по id");
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }
}
