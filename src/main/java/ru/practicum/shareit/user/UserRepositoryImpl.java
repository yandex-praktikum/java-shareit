package ru.practicum.shareit.user;

import lombok.*;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class UserRepositoryImpl implements UserRepository{
    Map<Long, User> usersMap = new HashMap<>();
    private Long currentId = 0L;

    @Override
    public List<UserDto> findAllUsers() {
        return null;
    }

    @Override
    public User create(User user) {
        user.setId(++currentId);
        usersMap.put(user.getId(), user);
        return usersMap.get(user.getId());
    }

    @Override
    public void deleteUser(Long id) {
    }

    @Override
    public User findUserById(Long userId) {
        return usersMap.get(userId);
    }
}
