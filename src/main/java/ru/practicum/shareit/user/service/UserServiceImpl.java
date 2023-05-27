package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exceptions.IncorrectUserParameterException;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUserModel(userDto);
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
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
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
