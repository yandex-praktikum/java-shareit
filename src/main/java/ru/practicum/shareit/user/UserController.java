package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.dto.UserMapper.*;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto add(@Valid @RequestBody User user) {
        log.info("UserController: POST /users, user={} ", user);
        return toUserDto(userService.add(user));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable int userId) {
        log.info("UserController: PATCH /users/{}, userDto={} ", userId, userDto);
        return toUserDto(userService.update(userDto, userId));
    }

    @DeleteMapping("/{userId}")
    public void delete(@Valid @PathVariable int userId) {
        log.info("UserController: DELETE /users/{}", userId);
        userService.delete(userId);
    }

    @GetMapping("/{userId}")
    public UserDto get(@Valid @PathVariable int userId) {
        log.info("UserController: GET /users/{}", userId);
        return toUserDto(userService.get(userId));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("UserController: GET /users/");
        return userService.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
