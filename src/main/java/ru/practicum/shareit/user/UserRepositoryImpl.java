package ru.practicum.shareit.user;

import lombok.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    Map<Long, User> usersMap = new HashMap<>();
    private Long currentId = 0L;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User create(User user) {
        user.setId(++currentId);
        usersMap.put(user.getId(), user);
        return usersMap.get(user.getId());
    }

    @Override
    public void deleteUser(Long userId) {
        usersMap.remove(userId);
    }

    @Override
    public User findUserById(Long userId) {
        return usersMap.get(userId);
    }

    @Override
    public User updateUser(User userDtoInUser) {
        usersMap.put(userDtoInUser.getId(), userDtoInUser);
        return usersMap.get(userDtoInUser.getId());
    }
}
