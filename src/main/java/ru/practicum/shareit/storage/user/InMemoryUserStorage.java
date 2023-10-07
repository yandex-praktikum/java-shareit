package ru.practicum.shareit.storage.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> userHashMap = new HashMap<>();
    private int generatedUserId = 1;

    @Override
    public User getUserById(Integer id) {
        if (userHashMap.get(id) == null) {
            return null;
        } else {
            return userHashMap.get(id);
        }
    }

    @Override
    public User addUser(User user) {
        if (!isDuplicateByEmail(user)) {
            user.setId(generatedUserId++);
            userHashMap.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Такой пользователь уже существует");
        }
    }

    @Override
    public User updateUser(User user, int userId) {
        if (userHashMap.get(userId) != null) {
            User userToUpdate = userHashMap.get(userId);
            List<User> allUsers = getAllUsers();

            boolean isDuplicate = false;

            for (User userInList : allUsers) {
                if (userInList.getEmail().equals(user.getEmail()) && userInList.getId() != userId) {
                    isDuplicate = true;
                }
            }
            if (isDuplicate) {
                throw new ValidationException("Такой e-mail присвоен другому пользователю");
            }
            if (user.getEmail() != null && !user.getEmail().equals(userToUpdate.getEmail())) {
                userToUpdate.setEmail(user.getEmail());
            }
            if (user.getName() != null && !user.getName().equals(userToUpdate.getName())) {
                userToUpdate.setName(user.getName());
            }
            return userHashMap.get(userId);

        } else {
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        Collection<User> allUsers = userHashMap.values();
        List<User> allUsersList = new ArrayList<>(allUsers);
        return allUsersList;
    }

    @Override
    public void deleteUser(int id) {
        if (userHashMap.get(id) == null) {
            throw new BadRequestException("Такого пользователя нет");
        } else {
            userHashMap.remove(id);
        }
    }

    @Override
    public boolean isDuplicateByEmail(User userToCheck) {
        List<User> users = getAllUsers();
        List<String> usersEmails = new ArrayList<>();
        for (User user : users) {
            usersEmails.add(user.getEmail());
        }
        if (usersEmails.contains(userToCheck.getEmail())) {
            return true;
        } else {
            return false;
        }
    }
}
