package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Request received POST /users: '{}'", userDto);
        return userService.createUser(UserMapper.fromUserDto(userDto));
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        log.info("Request received GET /users: with id = {}", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Request received GET /users");
        return userService.getUsers();
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Request received PATCH /users: with id = {}", userId);
        return userService.updateUser(UserMapper.fromUserDto(userDto).withId(userId));
    }

    @DeleteMapping("/{userId}")
    public void removeUserById(@PathVariable Long userId) {
        log.info("Request received DELETE /users with id = {}", userId);
        userService.removeUserById(userId);
    }
}
