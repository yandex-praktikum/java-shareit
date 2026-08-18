package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestBody @Valid UserCreateDto requestDto) {

        log.info("Creating user");

        return userClient.create(requestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(
            @PathVariable Long userId,
            @RequestBody @Valid UserUpdateDto requestDto) {

        log.info("Updating user {}", userId);

        return userClient.update(userId, requestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(
            @PathVariable Long userId) {

        log.info("Getting user {}", userId);

        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {

        log.info("Getting all users");

        return userClient.getAll();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(
            @PathVariable Long userId) {

        log.info("Deleting user {}", userId);

        return userClient.delete(userId);
    }
}