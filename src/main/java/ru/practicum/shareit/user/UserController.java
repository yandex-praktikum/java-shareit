package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
//use
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Long id) {
        log.info("DELETE request for user ID: {}", id);
        userService.delete(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("GET request for all users");
        return userService.findAll();
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable Long id, @RequestBody UserDto dto) {
        log.info("PATCH request for user ID: {}", id);
        return userService.update(id, dto);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        log.info("GET request for user ID: {}", id);
        return userService.getById(id);
    }

    @PostMapping
    public UserDto saveUser(@Valid @RequestBody UserDto dto) {
        log.info("POST request to create user: {}", dto.getEmail());
        return userService.create(dto);
    }
}