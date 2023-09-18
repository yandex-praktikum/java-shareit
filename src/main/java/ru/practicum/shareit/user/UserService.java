package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getUsers() {
        return userRepository.getUsers().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userRepository.getUser(userId));
    }

    public UserDto createUser(UserDto userDto) {
        UserValidator.checkAllFields(userDto);
        return UserMapper.toUserDto(userRepository.createUser(UserMapper.toUser(userDto)));
    }

    public UserDto updateUser(UserDto updatedUserDto) {
        UserValidator.checkNotNullFields(updatedUserDto);
        return UserMapper.toUserDto(userRepository.updateUser(UserMapper.toUser(updatedUserDto)));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
