package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override

    public UserDto addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public UserDto getUserDtoById(int userId) {
        return userDao.getUserDtoById(userId);
    }

    @Override
    public String removeUserById(int userId) {
        return userDao.removeUserById(userId);
    }

    @Override
    public UserDto UpdateUser(int userId, Map<Object, Object> fields) {
        return userDao.UpdateUser(userId, fields);
    }
}
