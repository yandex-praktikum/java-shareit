package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DuplicateException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.dto.UserDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto getById(Long id) {
        User user = repository.getUserById(id);
        if (user == null) {
            log.error("non-existent user with id " + id);
            throw new UserNotFoundException("can't find user with id " + id);
        } else {
            log.info("user " + id + " returned");
            return UserMapper.toDto(user);
        }
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> users = repository.findAllUsers().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        log.info("all users returned");
        return users;
    }

    @Transactional
    @Override
    public User create(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("email can't be empty");
        }
        try {
            User user = UserMapper.create(userDto);
            repository.save(user);
            log.info("user created");
            return user;
        } catch (DataIntegrityViolationException e) {
            log.warn("User " + userDto + " already exist");
            throw new DuplicateException("User " + userDto + " already exist");
        }
    }

    @Transactional
    @Override
    public User put(UserDto user, Long id) {
        User updatedUser = repository.getUserById(id);
        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        try {
            repository.save(updatedUser);
            log.info("user with id " + id + " updated");
            return updatedUser;
        } catch (DataIntegrityViolationException e) {
            log.warn("Email " + user.getEmail() + " already exist");
            throw new DuplicateException("User with email " + user.getEmail() + " already exist");
        }
    }

    @Transactional
    @Override
    public boolean delete(Long userId) {
        User deletedUser = repository.getUserById(userId);
        if (deletedUser != null) {
            repository.delete(deletedUser);
            log.info("user with id " + userId + " deleted");
            return true;
        } else {
            log.info("cant find user with id " + userId);
            throw new UserNotFoundException("can't find user with id " + userId);
        }
    }
}