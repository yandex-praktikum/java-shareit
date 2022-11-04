package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDaoStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDaoStorage storage;
    private final UserMapper mapper;

    @Override
    public UserDto create(UserDto userDto) {
        checkUserEmail(userDto.getEmail());
        User user = mapper.fromUserDtoCreate(userDto);
        User createdUser = storage.addToUserMap(user);
        return mapper.fromUser(createdUser);
    }

    @Override
    public Optional<UserDto> update(Long id, UserDto userDto) {
            checkUserEmail(userDto.getEmail());
            User user = mapper.fromUserDtoUpdate(userDto);
            User updatedUser = storage.update(id, user).orElseThrow(
                    () -> new IllegalArgumentException("Юзер с id" + id + "не найден"));
            return Optional.of(mapper.fromUser(updatedUser));
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        User user = storage.getUserById(id).orElseThrow(
                () -> new IllegalArgumentException("Юзер с id" + id + "не найден"));
        return Optional.of(mapper.fromUser(user));
    }

    @Override
    public List<UserDto> getAll() {
        List<User> responseUserList = storage.getAll();
        if (responseUserList.isEmpty()) {
            throw new IllegalArgumentException("Ни один юзер не найден");
        }
        return responseUserList.stream()
                .map(mapper::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id) {
        storage.deleteUserById(id);
    }

    private void checkUserEmail(String email) {
        List<String> emails = storage.getAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        if (!emails.isEmpty() && emails.contains(email)) {
            throw new RuntimeException("Юзер email" + email + " уже существует");
        }
    }
}
