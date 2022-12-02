package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.UserEmailValidationException;
import ru.practicum.shareit.exeptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.Validation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    public UserDto addUser(UserDto userDto) throws UserEmailValidationException {
        Validation.validateUserEmail(userStorage, userDto.getEmail());
        return userStorage.addUser(userDto);
    }

    public UserDto updateUser(Integer userId, UserDto userDto) throws UserEmailValidationException, ValidationException {
        Validation.validateUserEmail(userStorage, userDto.getEmail());
        Validation.validateUserId(userStorage, userId);
        if (userDto.getEmail() == null) {
            userDto.setEmail(userStorage.findUserDtoById(userId).getEmail());
        }
        if (userDto.getName() == null) {
            userDto.setName(userStorage.findUserDtoById(userId).getName());
        }
        return userStorage.updateUser(userId, userDto);
    }

    public List<UserDto> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public UserDto findUserById(Integer userId) throws ValidationException {
        Validation.validateUserId(userStorage, userId);
        return userStorage.findUserDtoById(userId);
    }

    public void deleteUserById(int userId) throws ValidationException {
        Validation.validateUserId(userStorage, userId);
        userStorage.deleteUserById(userId);
    }
}
