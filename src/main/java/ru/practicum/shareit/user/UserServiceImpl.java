package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.practicum.shareit.user.UserMapper.userDtoInUser;
import static ru.practicum.shareit.user.UserMapper.userInDTO;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Set<String> listEmail = new HashSet<>();

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public UserDto create(UserDto inputUserDto) {
        checkingEmailCreating(inputUserDto);
        User user = userRepository.create(userDtoInUser(inputUserDto));
        listEmail.add(inputUserDto.getEmail());
        return userInDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null){
            throw new NotFoundException("Пользователь не найден");
        }
        return userInDTO(user);
    }

    private void checkingEmailCreating(User inputUser) {
        if (listEmail.contains(inputUser.getEmail()))
            throw new ValidateException("Ошибка!Такой Email уже зарегестрирован");
    }
}
