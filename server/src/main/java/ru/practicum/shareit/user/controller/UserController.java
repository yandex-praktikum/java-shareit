package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid User user) {
        log.info("Получен запрос POST /users");
        return userService.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(@RequestBody User user, @PathVariable int userId) {
        log.info("Получен запрос PATCH /users/{userId}");
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("Получен запрос DELETE /users/{userId}");
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос GET /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.info("Получен запрос GET /users/{userId}");
        return userService.getUserById(userId);
    }
}
