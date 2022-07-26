package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class UserRepositoryImpl implements UserRepository {

    private static int id = 0;
    private static final HashMap<Integer, User> userList = new HashMap<>();

    @Override
    public User addUser(User user) {
        if (user.getId() == 0) {
            id++;
            user.setId(id);
        }
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        userList.remove(id);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userList.values());
    }

    @Override
    public User getUserById(int userId){
        if (userList.size() > 0){
            return userList.get(userId);
        } else {
            throw new NotFoundException("Список пользователей пуст");
        }
    }

}
