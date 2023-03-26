package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.UserEmailNotUniqueException;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * получить объект User по id
     *
     * @param userId id пользователя
     * @return объект User
     */
    public User getById(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Пользователь " + userId + " не найден");
        } else {
            return optionalUser.get();
        }
    }

    /**
     * получить всех пользователей
     *
     * @return список объектов типа User
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * создать нового пользователя в хранилище
     *
     * @param user заполненный объект User
     * @return заполненный объект User
     */
    public User create(User user) {
        User storageUser;
        try {
            storageUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserEmailNotUniqueException("E-mail не уникален");
        }

        return storageUser;
    }

    /**
     * изменить данные существующего пользователя в хранилище
     *
     * @param user объект User, частично или полностью заполненный данными
     * @return заполненный объект User
     */
    public User update(User user) {
        if (user != null && user.getId() > 0) {
            User storageUser = getById(user.getId());
            if (user.getEmail() != null && !user.getEmail().isBlank()) storageUser.setEmail(user.getEmail());
            if (user.getName() != null && !user.getName().isBlank()) storageUser.setName(user.getName());

            try {
                return userRepository.save(storageUser);
            } catch (DataIntegrityViolationException e) {
                throw new UserEmailNotUniqueException("E-mail не уникален");
            }
        }

        return null;
    }

    /**
     * удалить пользователя из хранилища
     *
     * @param userId id пользователя
     */
    public void delete(long userId) {
        userRepository.delete(getById(userId));
    }
}