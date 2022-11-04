package ru.practicum.shareit.user.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("{userId}")
    public Optional<UserDto> update(@PathVariable @Positive (message = "id не может быть отрицательным числом")
                                 Long userId, @NonNull @RequestBody UserDto userDto) {
        return userService.update(userId, userDto);
    }

    @GetMapping("{userId}")
    public Optional<UserDto> getUserById(@PathVariable @Positive (message = "id не может быть отрицательным числом")
                                      Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable @Positive (message = "id не может быть отрицательным числом")
                                   Long userId) {
        userService.deleteUserById(userId);
    }
}