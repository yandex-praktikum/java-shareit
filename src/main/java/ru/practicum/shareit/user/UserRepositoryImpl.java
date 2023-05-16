package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NoSuchUserException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Long, User> userHashMap = new HashMap<>();
    private long id = 1;

    @Override
    public List<UserDTO> getAll() {
        return userHashMap.values()
                .stream()
                .map(UserMapper::UserToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO get(long userId) {
        Optional<User> userOpt = Optional.ofNullable(userHashMap.get(userId));
        if(userOpt.isPresent()){
            return UserMapper.UserToDto(userOpt.get());
        }
        throw new NoSuchUserException(String.format("Пользователя с ID=%s не существует", userId));
    }
    @Override
    public UserDTO create(User user) {
        user.setId(id++);
        userHashMap.put(user.getId(), user);
        return UserMapper.UserToDto(user);
    }

    @Override
    public UserDTO update(User user, long userId) {

        if (userHashMap.containsKey(userId)) {
            User oldUser = userHashMap.get(userId);
            oldUser.setName(user.getName() == null ? oldUser.getName() : user.getName());
            oldUser.setEmail(user.getEmail() == null ? oldUser.getEmail() : user.getEmail());
            userHashMap.replace(userId, oldUser);
            return UserMapper.UserToDto(oldUser);
        }
        throw new NoSuchUserException(String.format("Неудалось обновить пользователя. " +
                "Пользователя с ID=%s не существует.", user.getId()));
    }

    @Override
    public UserDTO delete(long userId) {
        Optional<User> userOptional = Optional.ofNullable(userHashMap.remove(userId));
        if (userOptional.isPresent()) {
            return UserMapper.UserToDto(userOptional.get());
        }
        throw new NoSuchUserException(String.format("Неудалось удалить пользователя. " +
                "Пользователя с ID=%s не существует.", userId));
    }

    public boolean isUserExist(long userId) {
        return userHashMap.containsKey(userId);
    }
}
