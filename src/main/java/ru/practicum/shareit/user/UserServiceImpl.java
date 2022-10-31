package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEmailDto;
import ru.practicum.shareit.user.dto.UserNameDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDaoStorage storage;

    @Override
    public User create(UserDto userDto) {
        checkUserEmail(userDto.getEmail());
        return storage.create(userDto);
    }

    @Override
    public Optional<User> update(Long id, UserDto userDto) {
        if (userDto.getEmail() == null  &&  userDto.getName() != null) {
            UserNameDto userNameDto = new UserNameDto(userDto.getName());
            return updateName(id, userNameDto);
        } else if (userDto.getName() == null && userDto.getEmail() != null) {
            UserEmailDto userEmailDto = new UserEmailDto(userDto.getEmail());
            checkUserEmail(userEmailDto.getEmail());
            return updateEmail(id, userEmailDto);
        } else {
            checkUserEmail(userDto.getEmail());
            return Optional.of(storage.update(id, userDto)
                .orElseThrow(() -> new IllegalArgumentException("Юзер с id" + id + "не найден")));
        }
    }

    @Override
    public Optional<User> updateName(Long id, UserNameDto nameDto) {
        return Optional.of(storage.updateName(id, nameDto)
                .orElseThrow(() -> new IllegalArgumentException("Юзер с id" + id + "не найден")));
    }

    @Override
    public Optional<User> updateEmail(Long id, UserEmailDto emailDto) {
        return Optional.of(storage.updateEmail(id, emailDto)
                .orElseThrow(() -> new IllegalArgumentException("Юзер с id" + id + "не найден")));
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.of(storage.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Юзер с id" + id + "не найден")));
    }

    @Override
    public List<User> getAll() {
        List<User> responseUserList = storage.getAll();
        if (responseUserList.isEmpty()) {
            throw new IllegalArgumentException("Ни один юзер не найден");
        }
        return responseUserList;
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
