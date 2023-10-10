package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ExistEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong atomicId = new AtomicLong();

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public UserDto createUser(UserDto userDto) {
        boolean containsEmail = users.values().stream()
                .map(User::getEmail)
                .collect(Collectors.toList()).contains(userDto.getEmail());
        if (containsEmail) {
            throw new ExistEmailException();
        } else if (userDto.getEmail() == null || userDto.getEmail().isBlank() || !validate(userDto.getEmail().toUpperCase())) {
            throw new NotValidEmailException();
        } else {
            userDto.setId(atomicId.addAndGet(1));
            User user = UserMapper.mapUserDtoToUser(userDto);
            users.put(user.getId(), user);
            return UserMapper.mapUserToUserDto(user);
        }
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User userToUpdate = users.values().stream()
                .filter(user -> Objects.equals(user.getId(), userId))
                .findFirst().orElse(null);
        if (userToUpdate == null) {
            throw new NotFoundException("");
        }
        userToUpdate.setName(userDto.getName() != null ? userDto.getName() : userToUpdate.getName());
        if (users.values().stream()
                .filter(u -> u.getEmail().equals(userDto.getEmail()))
                .anyMatch(u -> !u.getId().equals(userId))) {
            throw new ConflictException();
        }
        userToUpdate.setEmail(userDto.getEmail() != null ? userDto.getEmail() : userToUpdate.getEmail());
        return UserMapper.mapUserToUserDto(userToUpdate);
    }

    @Override
    public User getUser(Long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getListOfUser() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }


    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }
}
