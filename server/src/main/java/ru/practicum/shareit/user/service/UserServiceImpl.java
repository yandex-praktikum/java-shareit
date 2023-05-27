package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.IncorrectUserParameterException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    public void deleteUser(Integer userId) {
        if (userId < 1) {
            log.error("Id пользователя должно быть больше 0");
            throw new UserNotFoundException("Id пользователя должно быть больше 0");
        }
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
    }

    public UserDto updateUser(Integer id, UserDto userDto) {
        User userByEmail = null;

        if (userDto.getEmail() != null) {
            userByEmail = userRepository.findByEmail(userDto.getEmail());
        }

        if (userByEmail == null || userByEmail.getId().equals(id)) {
            User user = userRepository.findById(id).get();
            if (userDto.getName() != null) {
                user.setName(userDto.getName());
            }

            if (userDto.getEmail() != null) {
                user.setEmail(userDto.getEmail());
            }
            userRepository.save(user);

            User newUser = userRepository.findById(id).get();
            return UserMapper.toUserDto(newUser);
        } else {
            log.error("Такой email уже существует");
            throw new IncorrectUserParameterException("Такой email уже существует");
        }
    }

    public List<UserDto> getUsersList() {
        return UserMapper.toUserDtoList(userRepository.findAll());
    }

    public UserDto getUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return UserMapper.toUserDto(user.get());
        } else {
            log.error("Пользователь не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
