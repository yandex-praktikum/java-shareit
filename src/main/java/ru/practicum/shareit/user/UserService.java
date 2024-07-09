package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MemoryUserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll().stream().toList();
    }

    public Optional<User> get(Long userId) {
        return userRepository.find(userId);
    }

    public User create(User user) {
        validate(user);
        checkIfMailAlreadyExist(user);

        user.setId(userRepository.nextId());
        userRepository.create(user);
        return user;
    }

    public User update(User newUser, Long userId) {
        User oldUser = validateUserId(userId);
        if (newUser.getEmail() != null && !oldUser.getEmail().equals(newUser.getEmail())) {
            checkIfMailAlreadyExist(newUser);
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getName() != null && !oldUser.getName().equals(newUser.getName())) {
            oldUser.setName(newUser.getName());
        }

        userRepository.update(oldUser);
        return oldUser;
    }

    public void delete(Long userId) {
        userRepository.delete(userId);
    }

    public User validateUserId(Long id) {
        return userRepository.find(id).orElseThrow(() -> new NotFoundException(id));
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().isBlank())
            throw new ValidationException("mail is empty");
        if (!user.getEmail().contains("@"))
            throw new ValidationException("mail must contains @");
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank())
            throw new ValidationException("login incorrect");
    }

    private void checkIfMailAlreadyExist(User user) {
        if (userRepository.getUsers().values().stream()
                .map(User::getEmail)
                .anyMatch(el -> el.equals(user.getEmail()))) {
            throw new DuplicateEmailException();
        }
    }
}
