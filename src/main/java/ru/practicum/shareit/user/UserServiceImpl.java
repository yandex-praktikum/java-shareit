package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User saveUser(@NotNull User user) {
        validateUser(user);
        return repository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return repository.getUserById(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        repository.removeUser(userId);
    }

    @Override
    public User updateUser(@NotNull UserDto userDto, Long userId) {
        validateUser(userDto.toUser());
        return repository.updateUser(userDto, userId);
    }


    private void validateUser(@NotNull User user) {
        getAllUsers().stream().filter(userIter -> userIter.getEmail().equals(user.getEmail())).forEach(userIter -> {
            throw new IllegalArgumentException();
        });
    }
}
