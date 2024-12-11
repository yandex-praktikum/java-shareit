package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.api.ValidateCreateRequest;
import ru.practicum.shareit.api.ValidateUpdateRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserSaveDto;
import ru.practicum.shareit.user.service.UserService;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    UserDto createUser(@RequestBody @Validated(ValidateCreateRequest.class) UserSaveDto userSaveDto) {
        return userService.createUser(userSaveDto);
    }

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@PathVariable Integer userId,
                       @RequestBody @Validated(ValidateUpdateRequest.class) UserSaveDto userSaveDto) {
        return userService.updateUser(userId, userSaveDto);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }
}
