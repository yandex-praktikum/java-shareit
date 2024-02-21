package ru.practicum.shareit.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.exception.FoundException;

import java.util.List;

import static ru.practicum.shareit.util.ValidationUtil.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User add(User user) {
        log.info("UserService: add({})", user);
        checkFound(user.getId(), String.valueOf(user.getId()));
        checkFound(userRepository.getByEmail(user.getEmail()), user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User update(UserDto userDto, int userId) {
        log.info("UserService: update({},{})", userDto, userId);
        User myUser = checkNotFoundWithId(get(userId), userId, "user");
        if (userDto.getName() != null) {
            myUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            User existMailUser = userRepository.getByEmail(userDto.getEmail());
            if (existMailUser != null && !existMailUser.getId().equals(userId)) {
                throw new FoundException(String.format("Email %s уже существует.", userDto.getEmail()));
            }
            myUser.setEmail(userDto.getEmail());
        }
        return userRepository.update(myUser);
    }

    @Override
    public void delete(int userId) {
        log.info("UserService: delete({})", userId);
        checkNotFoundWithId(userRepository.delete(userId), userId, "user");
    }

    @Override
    public User get(int userId) {
        log.info("UserService: get({})", userId);
        return checkNotFoundWithId(userRepository.get(userId), userId, "user");
    }

    @Override
    public List<User> getAll() {
        log.info("UserService: addAll()");
        return userRepository.getAll();
    }
}
