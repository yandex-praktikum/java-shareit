package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserStorage userStorage;

    @Override
    public UserDto get(Long id) {
        return userMapper.toUserDto(userStorage.get(id));
    }

    @Override
    public Collection<UserDto> getAll() {
        return userStorage.getAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto add(UserDto userDto) {
        User user = userStorage.add(userMapper.toUser(userDto));
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto patch(UserDto userDto, Long id) {
        userDto.setId(id);
        return userMapper.toUserDto((userStorage.update(userMapper.toUser(userDto))));
    }

    @Override
    public Boolean delete(Long id) {
        return userStorage.delete(id);
    }
}
