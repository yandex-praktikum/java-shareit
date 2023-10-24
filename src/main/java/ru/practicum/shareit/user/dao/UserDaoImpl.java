package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import ru.practicum.shareit.exception.EmailDuplicateException;
import ru.practicum.shareit.exception.NoDataFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int count = 0;
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto addUser(User user) {
        checkEmailForDuplicate(user.getEmail());
        user.setId(++count);
        UserDto userDto = userMapper.toUserDto(user);
        userMap.putIfAbsent(user.getId(), user);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userMap.values()) {
            userDtoList.add(userMapper.toUserDto(user));
        }
        return userDtoList;
    }

    @Override
    public User getUserById(int userId) {
        if (userMap.containsKey(userId)) {
            return userMap.get(userId);
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    @Override
    public UserDto getUserDtoById(int userId) {
        if (userMap.containsKey(userId)) {
            return userMapper.toUserDto(userMap.get(userId));
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    @Override
    public String removeUserById(int userId) {
        if (userMap.containsKey(userId)) {
            userMap.remove(userId);
            userMap.remove(userId);
            return "Пользователь с id:" + userId + " удалён.";
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    @Override
    public UserDto UpdateUser(int userId, Map<Object, Object> fields) {
        if (userMap.containsKey(userId)) {
            User user = getUserById(userId);
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, (String) key);
                if (((String) key).equalsIgnoreCase("email") && !user.getEmail()
                        .equalsIgnoreCase((String) value)) {
                    checkEmailForDuplicate((String) value);
                }
                field.setAccessible(true);
                ReflectionUtils.setField(field, user, value);
            });
            return userMapper.toUserDto(user);
        } else {
            throw new NoDataFoundException("Пользователь с id:" + userId + " не найден.");
        }
    }

    private void checkEmailForDuplicate(String email) {
        List<UserDto> userDtoList = getAllUsers();
        for (UserDto userDto : userDtoList) {
            if (userDto.getEmail().equalsIgnoreCase(email)) {
                throw new EmailDuplicateException("Email: " + email + " уже существует");
            }
        }
    }
}
