package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Component
@Lazy
public class UserRepositoryImpl implements UserRepository {

    private Integer userId = 0;

    private final Map<Integer, User> users = new HashMap<>();

    private final Set<String> emails = new HashSet<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public UserDto addUser(User user) {
        if (!emails.contains(user.getEmail())) {
            setUserId(getUserId() + 1);
            user.setId(getUserId());
            users.put(user.getId(), user);
            emails.add(user.getEmail());
            log.info("Пользователь создан");
        } else {
            log.info("Пользователь с таким e-mail уже cуществует");
            throw new DuplicateEmailException();
        }
        return UserMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUser(int userId, UserDto userDto) {
        User updatedUser;
        if (users.containsKey(userId)) {
            User user = users.get(userId);
            if (userDto.getName() == null) {
                updatedUser = updateEmail(user, userDto);
            } else if (userDto.getEmail() == null) {
                updatedUser = updateName(user, userDto);
            } else {
                updatedUser = updateNameAndEmail(user, userDto);
            }
            if (updatedUser != null) {
                users.remove(userId);
                users.put(userId, updatedUser);
                log.info("Пользовател обновлен");
            }
        } else {
            log.info("Пользователь с id = " + userId + " не найден");
            throw new UserNotFoundException();
        }
        return UserMapper.userToUserDto(updatedUser);
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void deleteUser(int id) {
        if (users.containsKey(id)) {
            emails.remove(users.get(id).getEmail());
            users.remove(id);
            log.info("Пользователь с id = " + id + " удален");
        } else {
            log.info("Пользователь с id = " + id + " не найден");
            throw new UserNotFoundException();
        }
    }

    private User updateEmail(User user, UserDto userDto) {
        if ((!emails.contains(userDto.getEmail())) || (user.getEmail().equals(userDto.getEmail()))) {
            emails.remove(user.getEmail());
            emails.add(userDto.getEmail());
            return User.builder()
                    .id(user.getId())
                    .email(userDto.getEmail())
                    .name(user.getName())
                    .build();
        } else {
            log.info("Пользователь с таким e-mail уже cуществует");
            throw new DuplicateEmailException();
        }
    }

    private User updateName(User user, UserDto userDto) {
        return User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(userDto.getName())
                .build();
    }

    private User updateNameAndEmail(User user, UserDto userDto) {
        if ((!emails.contains(userDto.getEmail())) || (user.getEmail().equals(userDto.getEmail()))) {
            emails.remove(user.getEmail());
            emails.add(userDto.getEmail());
            return User.builder()
                    .id(user.getId())
                    .email(userDto.getEmail())
                    .name(userDto.getName())
                    .build();
        } else {
            log.info("Пользователь с таким e-mail уже cуществует");
            throw new DuplicateEmailException();
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
