package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDto user) {
        log.info("Create user {}", user);
        return userClient.add(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@RequestBody UserDto user, @PathVariable int userId) {
        log.info("Update user {}, userId = {}", user, userId);
        return userClient.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@Positive @PathVariable int userId) {
        log.info("Delete user {}", userId);
        return userClient.delete(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get users");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable int userId) {
        log.info("Get user {}", userId);
        return userClient.getUser(userId);
    }
}
