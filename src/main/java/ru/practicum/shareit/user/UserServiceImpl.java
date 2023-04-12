package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static ru.practicum.shareit.user.UserMapper.userDtoInUser;
import static ru.practicum.shareit.user.UserMapper.userInDTO;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public UserDto create(UserDto inputUserDto) throws UserAlreadyExistsException {
        User user = userRepository.create(userDtoInUser(inputUserDto));
        return userInDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }
}
