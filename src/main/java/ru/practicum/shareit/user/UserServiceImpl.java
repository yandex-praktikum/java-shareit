package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.mopel.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User newUser = userMapper.toUser(userDto);
        User oldUser = userMapper.toUser(getUser(id));

        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        User user = userRepository.save(oldUser);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> {
                                      log.error("User with id " + id + " not found!");
                                      return new NotFoundException(
                                              "User with id " + id + " not found!"
                                      );
                                  });
        return userMapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        User user = userMapper.toUser(getUser(id));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
    }

}
