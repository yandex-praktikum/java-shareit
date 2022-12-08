package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User add(User user) {
        log.info("добавлен пользователь /{}/", user);
        return userRepository.save(user);

    }

    @Override
    public User update(User user, Integer id) {
        User userUpd = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getEmail() != null) {
            userUpd.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userUpd.setName(user.getName());
        }
        log.info("обновлен пользователь newValue=/{}/", userUpd.toString());
        return userRepository.save(userUpd);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
        log.info("удален пользователь /id={}/", id);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
        log.info("удалены все пользователи");
    }
}
