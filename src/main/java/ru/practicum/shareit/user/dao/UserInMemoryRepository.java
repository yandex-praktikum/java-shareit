package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserInMemoryRepository {
    private final List<User> listOfAllUsers = new ArrayList<>();

    public List<User> getAllUsers() {
        log.info("UserInMemoryRepository: Запрос на получение всех пользователей");
        return listOfAllUsers;
    }

    public User addNewUser(User user) {
        log.info("UserInMemoryRepository: Запрос на добавление нового пользователя {}", user);
        user.setId(getNextId());
        for (User userToCompare : listOfAllUsers) {
            if (userToCompare.getEmail().equals(user.getEmail())) {
                throw new ValidationException("У пользователей должны быть разные email");
            }
        }
        if (!listOfAllUsers.contains(user)) {
            listOfAllUsers.add(user);
            return user;
        }
        return null;
    }

    public User changeUser(long userId, User user) {
        log.info("UserInMemoryRepository: Запрос на обновление пользователя {}", user);
        for (User userToFind : listOfAllUsers) {
            if (userToFind.getId().equals(userId)) {
                if (user.getName() != null) {
                    userToFind.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    userToFind.setEmail(user.getEmail());
                }
                return userToFind;
            }
        }
        return null;
    }

    public Optional<User> getUserById(long userId) {
        log.info("UserInMemoryRepository: Запрос на получение пользователя с ID={}", userId);
        User userToBeFound = new User();
        for (User user : listOfAllUsers) {
            if (user.getId() == userId) {
                userToBeFound = user;
                return Optional.of(userToBeFound);
            }
        }
        return Optional.empty();
    }

    public void deleteUser(long userIdToDelete) {
        log.info("UserInMemoryRepository: Запрос на удаление пользователя с ID={}", userIdToDelete);
        listOfAllUsers.removeIf(user -> (user.getId() == userIdToDelete));
    }

    private long getNextId() {
        return listOfAllUsers.size() + 1;
    }

}
