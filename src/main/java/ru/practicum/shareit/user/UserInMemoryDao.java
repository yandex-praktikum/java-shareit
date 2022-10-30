package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserEmailDto;
import ru.practicum.shareit.user.dto.UserNameDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Component("userInMemoryDao")
public class UserInMemoryDao implements UserDaoStorage {

    private Map<Long,User> users;
    private final UserMapper mapper;

    @Override
    public User create(UserDto userDto) {
        User user = mapper.fromUserDto(userDto);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> update(Long id, UserDto userDto) {
        Optional<User> updatedUser = getUserById(id);
        if(updatedUser.isPresent()) {
            users.get(id).setName(userDto.getName());
            users.get(id).setEmail(userDto.getEmail());
            return getUserById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updateName(Long id, UserNameDto nameDto) {
        Optional<User> updatedUser = getUserById(id);
        if(updatedUser.isPresent()) {
            users.get(id).setName(nameDto.getName());
            return getUserById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> updateEmail(Long id, UserEmailDto emailDto) {
        Optional<User> updatedUser = getUserById(id);
        if(updatedUser.isPresent()) {
            users.get(id).setEmail(emailDto.getEmail());
            return getUserById(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        for(Long ids : users.keySet()) {
            if (ids.equals(id)) {
                return Optional.ofNullable(users.get(id));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(Long id) {
        Optional<User> user = getUserById(id);
        if(user.isPresent()) {
            users.remove(id, users.get(id));
        }
    }
}
