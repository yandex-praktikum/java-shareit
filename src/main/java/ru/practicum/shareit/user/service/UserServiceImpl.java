package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.OtherErrorException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserMapper userMapper;
    private static final String USER_NOT_FOUND_MESSAGE = "Пользователя с id %s нет";

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        checkUserEmailIsNotUnique(user, user.getId());

        return userMapper.toUserDto(userDao.create(user));
    }

    @Override
    public List<UserDto> findAll() {
        return userDao.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long userId) {
        User user = userDao.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId));
        });

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto updateById(UserDto userDto, Long userId) {
        User userFromMap = userMapper.toUser(findById(userId));
        User userFromDto = userMapper.toUser(userDto);

        checkUserIsNotExists(userFromMap, userId);
        checkUserEmailIsNotUnique(userFromDto, userId);

        userFromMap.setName(Objects.requireNonNullElse(userFromDto.getName(), userFromMap.getName()));
        userFromMap.setEmail(Objects.requireNonNullElse(userFromDto.getEmail(), userFromMap.getEmail()));

        return userMapper.toUserDto(userDao.update(userFromMap, userId));
    }

    @Override
    public void delete(Long userId) {
        User userFromMap = userMapper.toUser(findById(userId));

        checkUserIsNotExists(userFromMap, userId);
        userDao.delete(userId);
    }

    private void checkUserIsNotExists(User user, Long userId) {
        if (user == null) {
            throw new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId));
        }
    }

    private void checkUserEmailIsNotUnique(User user, Long userId) {
        List<UserDto> userWithSameEmail = findAll()
                .stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .filter(u -> !Objects.equals(u.getId(), userId))
                .collect(Collectors.toList());

        if (!userWithSameEmail.isEmpty()) {
            throw new OtherErrorException("Пользователь с такой почтой уже есть");
        }
    }
}