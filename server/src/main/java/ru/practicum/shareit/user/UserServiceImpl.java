package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userRepository.save(UserMapper.createUserDto(userDto));


        log.info("Пользователь добавлен {}",
                user);
        return UserMapper.converFromDto(user);
    }

    @Override
    public UserDto get(Long id) {
        return UserMapper.converFromDto(userRepository.findById(id)
                .orElseThrow(() -> exceptionFormat(id)));
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::converFromDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, long id) {
        User user = UserMapper.createUserDto(userDto);
        user.setId(id);

        if (userRepository.update(user.getId(),
                user.getEmail(),
                user.getName()) < 1)
            throw exceptionFormat(id);

        log.info("Пользователь c id {} обновлён", user.getId());

        return UserMapper.converFromDto(userRepository.findById(id).get());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public static NotFoundException exceptionFormat(Long id) {
        return new NotFoundException(String.format("Пользователь с id = %s, не найден", id));
    }
}
