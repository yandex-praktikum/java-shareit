package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EmailConflictException;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;

import java.util.*;

@Slf4j
@Repository
public class UserDaoInMemoryImpl implements UserDao {
    private long idGenerator;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public User create(User user) {
        checkEmailInStorage(user.getEmail());
        Long id = getNextId();
        user.setId(id);
        users.put(id, user);
        emails.add(user.getEmail());
        log.info("new user added: {}", user.getName());
        return user;
    }

    @Override
    public User update(long userId, User user) {
        checkUserInStorage(userId);

        User previousUser = users.get(userId);

        if (user.getName() != null) {
            previousUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().equals(previousUser.getEmail())) {
            checkEmailInStorage(user.getEmail());
            emails.remove(previousUser.getEmail());
            previousUser.setEmail(user.getEmail());
            emails.add(user.getEmail());
        }

        return previousUser;
    }

    @Override
    public User findById(long userId) {
        checkUserInStorage(userId);
        return users.get(userId);
    }

    @Override
    public void delete(long userId) {
        checkUserInStorage(userId);
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private Long getNextId() {
        return ++idGenerator;
    }

    private void checkUserInStorage(long userId) {
        if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException("user with id:" + userId + " not found error");
        }
    }

    private void checkEmailInStorage(String email) {
        if (emails.contains(email)) {
            throw new EmailConflictException("data validation error. email:" + email + " already in use");
        }
    }

    public void clearDataForTesting() {
        idGenerator = 0;
        users.clear();
        emails.clear();
    }
}