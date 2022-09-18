package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidation validation;

    @Override
    public User addUser(UserDto userDto) {
        validation.userIsValidAdd(userDto);
        User user = userMapper.userDtoToUser(userDto);
        return userRepository.add(user);
    }

    @Override
    public User updateUser(Long id, UserDto userDto) {

        validation.userIsValidUpdate(userDto);

        User user = findUser(id);
        User updatedUser = new User(id, user.getName(), user.getEmail());

        if (userDto.getEmail() != null) {
            updatedUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            updatedUser.setName(userDto.getName());
        }

        return userRepository.update(id, updatedUser);

    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public User findUser(Long id) {
        return userRepository.find(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
