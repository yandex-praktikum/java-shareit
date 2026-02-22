package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("GET /users");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        log.info("GET /users/{}", id);
        return userService.findById(id);
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("POST /users - создание пользователя");
        return userService.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("PATCH /users/{} - обновление пользователя", id);
        return userService.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("DELETE /users/{}", id);
        userService.delete(id);
    }
}