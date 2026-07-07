package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("GET /users - получение всех пользователей");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable long userId) {
        log.info("GET /users/{} - получение пользователя", userId);
        return userClient.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated(UserDto.OnCreate.class)
                                             @RequestBody UserDto userDto) {
        log.info("POST /users - создание пользователя: {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable long userId,
                                             @Validated(UserDto.OnUpdate.class)
                                             @RequestBody UserDto userDto) {
        log.info("PATCH /users/{} - обновление пользователя", userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        log.info("DELETE /users/{} - удаление пользователя", userId);
        return userClient.deleteUser(userId);
    }
}